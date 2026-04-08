package tech.framti.dashboard.nav

import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.scene.DialogSceneStrategy
import tech.framti.dashboard.ui.activate.ActivateViewRoot
import tech.framti.dashboard.ui.error.ErrorModalViewRoot
import tech.framti.dashboard.ui.main.MainViewRoot
import tech.framti.dashboard.ui.scratch.ScratchViewRoot
import tech.framti.navigation.EntryProviderInstaller
import tech.framti.navigation.screen.DashboardScreens

val provideDashboardScreens: EntryProviderInstaller = {
    entry<DashboardScreens.MainScreen> {
        MainViewRoot()
    }

    entry<DashboardScreens.ScratchScreen> {
        ScratchViewRoot()
    }

    entry<DashboardScreens.ActivationScreen> {
        ActivateViewRoot()
    }

    entry<DashboardScreens.ErrorModalScreen>(metadata = DialogSceneStrategy.dialog(DialogProperties())) { key ->
        ErrorModalViewRoot(title = key.title, subtitle = key.subtitle)
    }
}