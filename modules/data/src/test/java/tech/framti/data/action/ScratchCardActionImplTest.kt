package tech.framti.data.action

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import tech.framti.data.api.card.ScratchCardApi
import tech.framti.data.datastore.encrypted.EncryptedAppPrefs
import tech.framti.data.datastore.encrypted.serializer.AuthPreferences
import tech.framti.data.holder.ScratchCardHolder
import tech.framti.domain.exception.ActivationException
import tech.framti.domain.model.AuthState
import tech.framti.domain.model.ScratchCard

class ScratchCardActionImplTest {

    private val scratchCardApi: ScratchCardApi = mockk()
    private val encryptedAppPrefs: EncryptedAppPrefs = mockk(relaxUnitFun = true)
    private val scratchCardHolder: ScratchCardHolder = mockk(relaxUnitFun = true)

    private val authPreferencesFlow = MutableStateFlow(AuthPreferences(AuthState.UNSCRATCHED))
    private val scratchedCodeFlow = MutableStateFlow<String?>(null)

    private lateinit var sut: ScratchCardActionImpl

    @Before
    fun setUp() {
        every { encryptedAppPrefs.authPreferences } returns authPreferencesFlow
        every { scratchCardHolder.getNullableItemFlow() } returns scratchedCodeFlow

        sut = ScratchCardActionImpl(
            scratchCardApi = scratchCardApi,
            encryptedAppPrefs = encryptedAppPrefs,
            scratchCardHolder = scratchCardHolder
        )
    }

    @Test
    fun `activationState emits mapped auth state from preferences`() = runTest {
        sut.activationState.test {
            assertEquals(AuthState.UNSCRATCHED, awaitItem())

            authPreferencesFlow.value = AuthPreferences(AuthState.SCRATCHED)
            assertEquals(AuthState.SCRATCHED, awaitItem())

            authPreferencesFlow.value = AuthPreferences(AuthState.ACTIVATED)
            assertEquals(AuthState.ACTIVATED, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `scratchedCode emits null initially`() = runTest {
        sut.scratchedCode.test {
            assertNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `scratchedCode emits value after holder update`() = runTest {
        sut.scratchedCode.test {
            assertNull(awaitItem())

            scratchedCodeFlow.value = "test-uuid"
            assertEquals("test-uuid", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `scratchCard sets uuid on holder`() = runTest {
        sut.scratchCard()

        coVerify { scratchCardHolder.setItem(any()) }
    }

    @Test
    fun `scratchCard sets auth preferences to SCRATCHED`() = runTest {
        sut.scratchCard()

        coVerify {
            encryptedAppPrefs.setAuthPreferences(AuthPreferences(AuthState.SCRATCHED))
        }
    }

    @Test
    fun `activate sets auth preferences to ACTIVATED when response android is above threshold`() =
        runTest {
            coEvery { scratchCardApi.activate("test-code") } returns ScratchCard(android = 300000)

            sut.activate("test-code")

            coVerify {
                encryptedAppPrefs.setAuthPreferences(AuthPreferences(AuthState.ACTIVATED))
            }
        }

    @Test(expected = ActivationException::class)
    fun `activate throws ActivationException when response android is below threshold`() =
        runTest {
            coEvery { scratchCardApi.activate("test-code") } returns ScratchCard(android = 100000)

            sut.activate("test-code")
        }

    @Test(expected = ActivationException::class)
    fun `activate throws ActivationException when response android equals threshold`() =
        runTest {
            coEvery { scratchCardApi.activate("test-code") } returns ScratchCard(android = 277028)

            sut.activate("test-code")
        }

    @Test
    fun `deactivate resets scratch card holder`() = runTest {
        sut.deactivate()

        verify { scratchCardHolder.reset() }
    }

    @Test
    fun `deactivate sets auth preferences to UNSCRATCHED`() = runTest {
        sut.deactivate()

        coVerify {
            encryptedAppPrefs.setAuthPreferences(AuthPreferences(AuthState.UNSCRATCHED))
        }
    }
}


