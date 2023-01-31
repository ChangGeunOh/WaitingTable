package kr.pe.paran.waiting.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.pe.paran.waiting.presentation.screen.advertising.AdvertisingScreen
import kr.pe.paran.waiting.presentation.screen.display.DisplayScreen
import kr.pe.paran.waiting.presentation.screen.image.ImageScreen
import kr.pe.paran.waiting.presentation.screen.image.detail.ImageDetailScreen
import kr.pe.paran.waiting.presentation.screen.manager.ManagerScreen
import kr.pe.paran.waiting.presentation.screen.numberpad.NumberPadScreen
import kr.pe.paran.waiting.presentation.screen.setting.SettingScreen
import kr.pe.paran.waiting.presentation.screen.splash.SplashScreen

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalMaterial3WindowSizeClassApi
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    LaunchedEffect(key1 = Unit, block = {
        Navigator.navSharedFlow.onEach {
            if (it.popBackStack) {
                navHostController.popBackStack()
            } else {
                navHostController.navigate(route = it.route) {
                    if (it.popUpTo.isNotEmpty()) {
                        popUpTo(route = it.popUpTo) {
                            inclusive = it.inclusive
                        }
                    }
                    launchSingleTop = it.launchSingleTop
                }
            }

        }.launchIn(this)
    })

    NavHost(
        navController = navHostController,
        startDestination = Screen.SplashScreen.route,
        builder = {
            composable(Screen.SplashScreen.route) {
                SplashScreen()
            }
            composable(Screen.AdvertisingScreen.route) {
                AdvertisingScreen()
            }
            composable(Screen.NumberPadScreen.route) {
                NumberPadScreen()
            }
            composable(Screen.SettingScreen.route) {
                SettingScreen()
            }
            composable(Screen.DisplayScreen.route) {
                DisplayScreen()
            }
            composable(Screen.ManagerScreen.route) {
                ManagerScreen()
            }


            navigation(startDestination = Screen.ImageScreen.route, route = "image") {
                composable(Screen.ImageScreen.route) {
                    ImageScreen()
                }

                composable(
                    route = Screen.ImageDetailScreen.route,
                    arguments = listOf(
                        navArgument(
                            name = "index",
                            builder = { type = NavType.IntType }
                        )
                    )
                ) {
                    ImageDetailScreen()
                }
            }
        }
    )
}