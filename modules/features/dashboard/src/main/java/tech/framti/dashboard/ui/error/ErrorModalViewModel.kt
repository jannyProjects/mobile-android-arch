package tech.framti.dashboard.ui.error

import dagger.hilt.android.lifecycle.HiltViewModel
import tech.framti.common.base.BaseViewModel
import tech.framti.common.base.Event
import tech.framti.common.base.ViewState
import tech.framti.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class ErrorModalViewModel @Inject constructor(
    private val navigator: Navigator
) : BaseViewModel<UiState, UiEvent>(navigator, UiState) {

    override fun onViewEvent(event: UiEvent) {
        when (event) {
            UiEvent.Dismiss -> execute {
                navigator.navigateBack()
            }
        }
    }
}

data object UiState: ViewState

sealed interface UiEvent : Event {
    data object Dismiss : UiEvent
}

