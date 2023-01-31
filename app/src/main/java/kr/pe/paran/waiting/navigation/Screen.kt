package kr.pe.paran.waiting.navigation

sealed class Screen(val route: String) {

    object PopBackStack: Screen("pop_back_stack")

    object SplashScreen: Screen("splash_screen")
    object MainScreen : Screen("main_screen")
    object AdvertisingScreen: Screen("ad_screen")
    object NumberPadScreen: Screen("number_pad_screen")
    object SettingScreen: Screen("setting_screen")
    object DisplayScreen: Screen("display_screen")

    object ManagerScreen: Screen("manager_screen")
    object ImageScreen: Screen("image_screen")
    object ImageDetailScreen: Screen("image_detail_screen/{index}") {
        fun passIndex(index: Int): String  = "image_detail_screen/$index"
    }
}
