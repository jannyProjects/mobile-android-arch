package tech.framti.common.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.framti.navigation.Navigator
import tech.framti.navigation.screen.DashboardScreens
import timber.log.Timber

abstract class BaseViewModel<S : ViewState, E : Event>(
    private val navigator: Navigator,
    initialState: S
) : ViewModel() {

    protected val _state = mutableStateOf(initialState)
    val state: State<S> = _state

    protected val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    fun emitState(newState: S) {
        _state.value = newState
    }

    fun execute(
        applyLoading: Boolean = true,
        onStart: (suspend () -> Unit)? = { if (applyLoading) _loadingState.value = true },
        onFinally: (() -> Unit)? = { if (applyLoading) _loadingState.value = false },
        onError: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                onStart?.invoke()
                block()
            } catch (ce: CancellationException) {
                // Propagate coroutine cancellation
                throw ce
            } catch (t: Throwable) {
                Timber.e(t)
                onError?.invoke(t)
            } finally {
                onFinally?.invoke()
            }
        }
    }

    abstract fun onViewEvent(event: E)

}

interface ViewState

interface Event