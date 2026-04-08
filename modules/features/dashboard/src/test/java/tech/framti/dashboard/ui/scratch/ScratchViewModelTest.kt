package tech.framti.dashboard.ui.scratch

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import tech.framti.domain.action.ScratchCardAction
import tech.framti.navigation.Navigator

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val navigator: Navigator = mockk(relaxUnitFun = true)
    private val scratchCardAction: ScratchCardAction = mockk(relaxUnitFun = true)

    private val scratchedCodeFlow = MutableStateFlow<String?>(null)

    private lateinit var sut: ScratchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { scratchCardAction.scratchedCode } returns scratchedCodeFlow

        sut = ScratchViewModel(navigator, scratchCardAction)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has null code`() {
        assertNull(sut.state.value.code)
    }

    @Test
    fun `init collects scratchedCode and updates code in state`() {
        scratchedCodeFlow.value = "test-uuid-123"

        assertEquals("test-uuid-123", sut.state.value.code)
    }

    @Test
    fun `init updates state when scratchedCode changes`() {
        scratchedCodeFlow.value = "first-uuid"
        assertEquals("first-uuid", sut.state.value.code)

        scratchedCodeFlow.value = "second-uuid"
        assertEquals("second-uuid", sut.state.value.code)
    }

    @Test
    fun `init resets code when scratchedCode emits null`() {
        scratchedCodeFlow.value = "some-uuid"
        assertEquals("some-uuid", sut.state.value.code)

        scratchedCodeFlow.value = null
        assertNull(sut.state.value.code)
    }

    @Test
    fun `GenerateNewCode event calls scratchCardAction scratchCard`() {
        sut.onViewEvent(UiEvent.GenerateNewCode)

        coVerify { scratchCardAction.scratchCard() }
    }

    @Test
    fun `NavigateBack event calls navigator navigateBack`() {
        sut.onViewEvent(UiEvent.NavigateBack)

        verify { navigator.navigateBack() }
    }
}

