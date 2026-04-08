package tech.framti.dashboard.ui.activate

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.framti.common.base.BaseViewModel
import tech.framti.common.base.Event
import tech.framti.common.base.ViewState
import tech.framti.dashboard.R
import tech.framti.domain.action.ScratchCardAction
import tech.framti.domain.model.AuthState
import tech.framti.navigation.Navigator
import tech.framti.navigation.screen.DashboardScreens
import javax.inject.Inject

@HiltViewModel
class ActivateViewModel @Inject constructor(
    private val navigator: Navigator,
    private val scratchCardAction: ScratchCardAction
) : BaseViewModel<UiState, UiEvent>(navigator, UiState()) {

    init {
        viewModelScope.launch {
            scratchCardAction.activationState
                .onEach {
                    emitState(state.value.copy(authState = it))
                }
                .collect()
        }
    }

    override fun onViewEvent(event: UiEvent) {
        when (event) {
            UiEvent.NavigateBack -> execute {
                navigator.navigateBack()
            }

            UiEvent.Activate -> execute(
                onError = {
                    navigator.navigateTo(
                        DashboardScreens.ErrorModalScreen(
                            R.string.general_error_title,
                            R.string.general_error_subtitle
                        )
                    )
                }
            ) {
                // exception will be catched and show error dlg
                val code = scratchCardAction.scratchedCode.first()!!
                withContext(NonCancellable) {
                    scratchCardAction.activate(code)
                }
            }
        }
    }
}

data class UiState(
    val authState: AuthState = AuthState.UNSCRATCHED
) : ViewState {
    val canActivate = authState != AuthState.ACTIVATED
}

sealed interface UiEvent : Event {
    data object NavigateBack : UiEvent
    data object Activate : UiEvent
}