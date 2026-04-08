package tech.framti.dashboard.ui.main

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import tech.framti.dashboard.R
import tech.framti.domain.action.ScratchCardAction
import tech.framti.domain.exception.ActivationException
import tech.framti.domain.model.AuthState
import tech.framti.navigation.Navigator
import tech.framti.navigation.screen.DashboardScreens

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val navigator: Navigator = mockk(relaxUnitFun = true)
    private val scratchCardAction: ScratchCardAction = mockk(relaxUnitFun = true)

    private val scratchedCodeFlow = MutableStateFlow<String?>(null)
    private val activationStateFlow = MutableStateFlow(AuthState.UNSCRATCHED)

    private lateinit var sut: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { scratchCardAction.scratchedCode } returns scratchedCodeFlow
        every { scratchCardAction.activationState } returns activationStateFlow

        sut = MainViewModel(navigator, scratchCardAction)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has UNSCRATCHED auth state`() {
        assertEquals(AuthState.UNSCRATCHED, sut.state.value.authState)
    }

    @Test
    fun `initial state has null generatedUuid`() {
        assertNull(sut.state.value.generatedUuid)
    }

    @Test
    fun `init collects scratchedCode and updates generatedUuid`() {
        scratchedCodeFlow.value = "test-uuid"

        assertEquals("test-uuid", sut.state.value.generatedUuid)
    }

    @Test
    fun `init collects activationState and updates authState`() {
        activationStateFlow.value = AuthState.SCRATCHED

        assertEquals(AuthState.SCRATCHED, sut.state.value.authState)
    }

    @Test
    fun `init collects activated state`() {
        activationStateFlow.value = AuthState.ACTIVATED

        assertEquals(AuthState.ACTIVATED, sut.state.value.authState)
    }

    @Test
    fun `init clears generatedUuid when scratchedCode emits null`() {
        scratchedCodeFlow.value = "some-code"
        assertEquals("some-code", sut.state.value.generatedUuid)

        scratchedCodeFlow.value = null
        assertNull(sut.state.value.generatedUuid)
    }

    @Test
    fun `canActivate is false when generatedUuid is null`() {
        val state = UiState(generatedUuid = null, authState = AuthState.UNSCRATCHED)
        assertFalse(state.canActivate)
    }

    @Test
    fun `canActivate is false when authState is ACTIVATED`() {
        val state = UiState(generatedUuid = "uuid", authState = AuthState.ACTIVATED)
        assertFalse(state.canActivate)
    }

    @Test
    fun `canActivate is true when generatedUuid present and state is UNSCRATCHED`() {
        val state = UiState(generatedUuid = "uuid", authState = AuthState.UNSCRATCHED)
        assertTrue(state.canActivate)
    }

    @Test
    fun `canActivate is true when generatedUuid present and state is SCRATCHED`() {
        val state = UiState(generatedUuid = "uuid", authState = AuthState.SCRATCHED)
        assertTrue(state.canActivate)
    }

    @Test
    fun `canActivate is false when generatedUuid is null and state is ACTIVATED`() {
        val state = UiState(generatedUuid = null, authState = AuthState.ACTIVATED)
        assertFalse(state.canActivate)
    }

    @Test
    fun `Deactivate event calls scratchCardAction deactivate`() {
        sut.onViewEvent(UiEvent.Deactivate)

        coVerify { scratchCardAction.deactivate() }
    }

    @Test
    fun `NavigateToScratch event navigates to ScratchScreen`() {
        sut.onViewEvent(UiEvent.NavigateToScratch)

        verify { navigator.navigateTo(DashboardScreens.ScratchScreen) }
    }

    @Test
    fun `NavigateToActivate event navigates to ActivationScreen`() {
        sut.onViewEvent(UiEvent.NavigateToActivate)

        verify { navigator.navigateTo(DashboardScreens.ActivationScreen) }
    }

    @Test
    fun `NavigateToActivate navigates to activation error screen on ActivationException`() {
        every {
            navigator.navigateTo(DashboardScreens.ActivationScreen)
        } throws ActivationException()

        sut.onViewEvent(UiEvent.NavigateToActivate)

        verify {
            navigator.navigateTo(
                DashboardScreens.ErrorModalScreen(
                    R.string.activation_error_title,
                    R.string.activation_error_subtitle
                )
            )
        }
    }

    @Test
    fun `NavigateToActivate navigates to general error screen on generic exception`() {
        every {
            navigator.navigateTo(DashboardScreens.ActivationScreen)
        } throws RuntimeException("unexpected")

        sut.onViewEvent(UiEvent.NavigateToActivate)

        verify {
            navigator.navigateTo(
                DashboardScreens.ErrorModalScreen(
                    R.string.general_error_title,
                    R.string.general_error_subtitle
                )
            )
        }
    }
}

