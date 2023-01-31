package kr.pe.paran.waiting.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kr.pe.paran.waiting.common.utils.Logcat

class Navigator {

    fun navigate(navigatorData: NavigatorData) {
        navMutableSharedFlow.tryEmit(navigatorData)
    }

    fun navigate(screen: Screen) {
        Logcat.i("navigate>$screen")
        navMutableSharedFlow.tryEmit(NavigatorData(route = screen.route))
    }

    fun navigate(route: String) {
        navMutableSharedFlow.tryEmit(NavigatorData(route = route))
    }

    fun popBackStack() {
        navMutableSharedFlow.tryEmit(NavigatorData(popBackStack = true))
    }

    companion object {
        private val navMutableSharedFlow = MutableSharedFlow<NavigatorData>(extraBufferCapacity = 1)
        val navSharedFlow = navMutableSharedFlow.asSharedFlow()
    }
}

