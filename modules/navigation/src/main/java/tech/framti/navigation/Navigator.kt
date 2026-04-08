package tech.framti.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

class Navigator @Inject constructor(val startDestination: NavKey) {

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startDestination)
    private val _navigationResults = MutableSharedFlow<BackNavigationResult>(replay = 1)
    val navigationResults: SharedFlow<BackNavigationResult> = _navigationResults

    fun navigateTo(destination: NavKey, setAsNewRoot: Boolean = false) {
        if (setAsNewRoot) {
            backStack.clear()
        }
        if (backStack.lastOrNull() != destination) {
            backStack.add(destination)
        }
    }

    /**
     * Navigates to the destination and removes the current screen from the backstack.
     * Useful for flows like Login -> Home where you don't want to go back to Login.
     */
    fun navigateAndReplaceCurrent(destination: NavKey) {
        backStack.removeLastOrNull()
        backStack.add(destination)
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    fun dismissScreenIfExists(destination: NavKey) {
        if (backStack.size > 1 && backStack.lastOrNull() == destination) {
            backStack.removeLastOrNull()
        }
    }

    fun navigateBackWithResult(result: BackNavigationResult) {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
            _navigationResults.tryEmit(result)
        }
    }

    /**
     * Pops the backstack to the specified destination.
     * @param destination The destination to pop to
     * @param inclusive If true, also removes the destination itself
     * @return true if the destination was found and navigation occurred, false otherwise
     */
    fun popTo(destination: NavKey, inclusive: Boolean = false): Boolean {
        val index = backStack.indexOfLast { it == destination }
        if (index == -1) return false

        val targetIndex = if (inclusive) index else index + 1
        while (backStack.size > targetIndex && backStack.size > 1) {
            backStack.removeLastOrNull()
        }
        return true
    }

    fun reset() {
        navigateTo(startDestination, true)
    }
}

inline fun <reified T : BackNavigationResult> SharedFlow<BackNavigationResult>.consumeResultFor() =
    this.filterIsInstance(T::class)