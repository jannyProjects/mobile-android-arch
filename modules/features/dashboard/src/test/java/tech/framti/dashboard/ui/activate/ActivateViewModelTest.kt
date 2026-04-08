package tech.framti.dashboard.ui.activate

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import tech.framti.dashboard.R
import tech.framti.domain.action.ScratchCardAction
import tech.framti.domain.model.AuthState
import tech.framti.navigation.Navigator
import tech.framti.navigation.screen.DashboardScreens

@OptIn(ExperimentalCoroutinesApi::class)
class ActivateViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val navigator: Navigator = mockk(relaxUnitFun = true)
    private val scratchCardAction: ScratchCardAction = mockk(relaxUnitFun = true)

    private val activationStateFlow = MutableStateFlow(AuthState.UNSCRATCHED)
    private val scratchedCodeFlow = MutableStateFlow<String?>("default-code")

    private lateinit var sut: ActivateViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { scratchCardAction.activationState } returns activationStateFlow
        every { scratchCardAction.scratchedCode } returns scratchedCodeFlow

        sut = ActivateViewModel(navigator, scratchCardAction)
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
    fun `init collects activationState and updates authState`() {
        activationStateFlow.value = AuthState.SCRATCHED

        assertEquals(AuthState.SCRATCHED, sut.state.value.authState)
    }

    @Test
    fun `init collects ACTIVATED state`() {
        activationStateFlow.value = AuthState.ACTIVATED

        assertEquals(AuthState.ACTIVATED, sut.state.value.authState)
    }

    @Test
    fun `canActivate is true when authState is UNSCRATCHED`() {
        val state = UiState(authState = AuthState.UNSCRATCHED)
        assertTrue(state.canActivate)
    }

    @Test
    fun `canActivate is true when authState is SCRATCHED`() {
        val state = UiState(authState = AuthState.SCRATCHED)
        assertTrue(state.canActivate)
    }

    @Test
    fun `canActivate is false when authState is ACTIVATED`() {
        val state = UiState(authState = AuthState.ACTIVATED)
        assertFalse(state.canActivate)
    }

    @Test
    fun `NavigateBack event calls navigator navigateBack`() {
        sut.onViewEvent(UiEvent.NavigateBack)

        verify { navigator.navigateBack() }
    }

    @Test
    fun `Activate event calls scratchCardAction activate with scratched code`() {
        scratchedCodeFlow.value = "my-scratch-code"

        sut.onViewEvent(UiEvent.Activate)

        coVerify { scratchCardAction.activate("my-scratch-code") }
    }

    @Test
    fun `Activate event reads code from scratchedCode flow`() {
        scratchedCodeFlow.value = "another-code"

        sut.onViewEvent(UiEvent.Activate)

        coVerify { scratchCardAction.activate("another-code") }
    }

    @Test
    fun `Activate event navigates to error screen when activate throws`() {
        scratchedCodeFlow.value = "code"
        coEvery { scratchCardAction.activate(any()) } throws RuntimeException("API failure")

        sut.onViewEvent(UiEvent.Activate)

        verify {
            navigator.navigateTo(
                DashboardScreens.ErrorModalScreen(
                    R.string.general_error_title,
                    R.string.general_error_subtitle
                )
            )
        }
    }

    @Test
    fun `Activate event navigates to error screen when scratchedCode is null`() {
        scratchedCodeFlow.value = null

        sut.onViewEvent(UiEvent.Activate)

        verify {
            navigator.navigateTo(
                DashboardScreens.ErrorModalScreen(
                    R.string.general_error_title,
                    R.string.general_error_subtitle
                )
            )
        }
    }

    @Test
    fun `Activate event does not navigate to error screen on success`() {
        scratchedCodeFlow.value = "valid-code"

        sut.onViewEvent(UiEvent.Activate)

        verify(exactly = 0) {
            navigator.navigateTo(match { it is DashboardScreens.ErrorModalScreen })
        }
    }
}

