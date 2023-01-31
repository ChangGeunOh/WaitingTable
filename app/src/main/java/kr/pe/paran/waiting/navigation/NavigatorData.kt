package kr.pe.paran.waiting.navigation

data class NavigatorData(
    val route: String = "",
    val argument: String = "",
    val popUpTo: String  = "",
    val inclusive: Boolean = false,
    val launchSingleTop: Boolean = false,
    val popBackStack : Boolean = false,
)