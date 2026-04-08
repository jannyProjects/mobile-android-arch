package tech.framti.dashboard.ui.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.framti.common.base.BaseViewModel
import tech.framti.common.base.Event
import tech.framti.common.base.ViewState
import tech.framti.dashboard.R
import tech.framti.domain.action.ScratchCardAction
import tech.framti.domain.exception.ActivationException
import tech.framti.domain.model.AuthState
import tech.framti.navigation.Navigator
import tech.framti.navigation.screen.DashboardScreens
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigator: Navigator, private val scratchCardAction: ScratchCardAction
) : BaseViewModel<UiState, UiEvent>(navigator, UiState()) {

    init {
        viewModelScope.launch {
            launch {
                scratchCardAction.scratchedCode.onEach {
                        emitState(state.value.copy(generatedUuid = it))
                    }.collect()
            }
            launch {
                scratchCardAction.activationState.onEach {
                        emitState(state.value.copy(authState = it))
                    }.collect()
            }
        }
    }

    override fun onViewEvent(event: UiEvent) {
        when (event) {
            UiEvent.Deactivate -> onDeactivate()
            UiEvent.NavigateToActivate -> onNavigateToActivate()
            UiEvent.NavigateToScratch -> onNavigateToScratch()
        }
    }

    private fun onDeactivate() = execute {
        scratchCardAction.deactivate()
    }

    private fun onNavigateToActivate() = execute(
        onError = {
            val screen = when (it) {
                is ActivationException -> DashboardScreens.ErrorModalScreen(
                    R.string.activation_error_title, R.string.activation_error_subtitle
                )

                else -> DashboardScreens.ErrorModalScreen(
                    R.string.general_error_title, R.string.general_error_subtitle
                )
            }
            navigator.navigateTo(screen)
        }) {
        navigator.navigateTo(DashboardScreens.ActivationScreen)
    }

    private fun onNavigateToScratch() = execute {
        navigator.navigateTo(DashboardScreens.ScratchScreen)
    }
}

data class UiState(
    val authState: AuthState = AuthState.UNSCRATCHED, val generatedUuid: String? = null
) : ViewState {
    val canActivate = generatedUuid != null && authState != AuthState.ACTIVATED
}

sealed interface UiEvent : Event {
    data object NavigateToScratch : UiEvent
    data object NavigateToActivate : UiEvent
    data object Deactivate : UiEvent
}