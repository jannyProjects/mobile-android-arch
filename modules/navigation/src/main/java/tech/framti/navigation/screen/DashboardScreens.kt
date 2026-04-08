package tech.framti.navigation.screen

import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface DashboardScreens: NavKey {

    @Serializable
    data object MainScreen: DashboardScreens
    @Serializable
    data object ScratchScreen: DashboardScreens

    @Serializable
    data object ActivationScreen: DashboardScreens

    @Serializable
    data class ErrorModalScreen(@param:StringRes val title: Int, @param:StringRes val subtitle: Int): DashboardScreens
}