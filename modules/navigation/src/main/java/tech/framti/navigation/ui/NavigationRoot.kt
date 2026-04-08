package tech.framti.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    backStack: List<NavKey>,
    entryProvider: (key: NavKey) -> NavEntry<NavKey>
) {
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = dialogStrategy,
        entryProvider = entryProvider
    )
}