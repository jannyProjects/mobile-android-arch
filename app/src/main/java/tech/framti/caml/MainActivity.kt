package tech.framti.caml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.entryProvider
import dagger.hilt.android.AndroidEntryPoint
import tech.framti.dashboard.nav.provideDashboardScreens
import tech.framti.navigation.Navigator
import tech.framti.navigation.ui.NavigationRoot
import tech.framti.theme.CamlTheme
import tech.framti.theme.whiteBg
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CamlTheme {
                Scaffold(
                    modifier = Modifier
                        .background(whiteBg)
                        .fillMaxSize()
                        .safeContentPadding()
                ) { _ ->
                    NavigationRoot(
                        backStack = navigator.backStack,
                        modifier = Modifier,
                        entryProvider = entryProvider {
                            provideDashboardScreens.invoke(this)
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CamlTheme {
    }
}