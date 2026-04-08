package tech.framti.dashboard.ui.scratch

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.framti.common.base.BaseViewModel
import tech.framti.common.base.Event
import tech.framti.common.base.ViewState
import tech.framti.domain.action.ScratchCardAction
import tech.framti.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class ScratchViewModel @Inject constructor(
    private val navigator: Navigator,
    private val scratchCardAction: ScratchCardAction
) : BaseViewModel<UiState, UiEvent>(navigator, UiState()) {

    init {
        viewModelScope.launch {
            scratchCardAction.scratchedCode
                .onEach {
                    emitState(state.value.copy(code = it))
                }
                .collect()
        }
    }

    override fun onViewEvent(event: UiEvent) {
        when (event) {
            UiEvent.GenerateNewCode -> execute {
                scratchCardAction.scratchCard()
            }

            UiEvent.NavigateBack -> execute {
                navigator.navigateBack()
            }
        }
    }
}

data class UiState(
    val code: String? = null,
) : ViewState

sealed interface UiEvent : Event {
    data object NavigateBack : UiEvent
    data object GenerateNewCode : UiEvent
}