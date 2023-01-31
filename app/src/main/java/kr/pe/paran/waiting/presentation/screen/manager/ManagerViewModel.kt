package kr.pe.paran.waiting.presentation.screen.manager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.common.utils.NetworkUtils.flowNetworkInfo
import kr.pe.paran.waiting.data.remote.Network
import kr.pe.paran.waiting.data.remote.NetworkStatus
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.Screen
import kr.pe.paran.waiting.presentation.screen.manager.contents.ManagerWaitingNumber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    @ApplicationContext application: Context,
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    var managerWaitingNumber = usesCases.managerWaitingUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ManagerWaitingNumber()
    )

    val networkInfo =
        application.flowNetworkInfo().combine(Network.networkStatus) { networkInfo, networkStatus ->
            networkInfo.serverAddress = Network.serverAddress.value
            networkInfo.isLiveServer = networkStatus is NetworkStatus.Success
            networkInfo
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkInfo()
        )


    private fun initManagerWaitingNumber() {

        viewModelScope.launch {
            val currentReceiptNumberData =
                usesCases.receiptNumberUseCase(waitingStatus = WaitingStatus.CALLING)
            val nextReceiptNumberData = usesCases.receiptNumberUseCase(isFirst = true)
            val countWaiting = usesCases.waitingCountUseCase().firstOrNull() ?: 0

            val managerWaitingNumberData = ManagerWaitingNumber(
                currentReceiptNumberData = currentReceiptNumberData,
                nextReceiptNumberData = nextReceiptNumberData,
                countWaiting = countWaiting
            )

            Logcat.i("initManagerWaitingNumber>$managerWaitingNumberData")
            managerWaitingNumber = usesCases.managerWaitingUseCase().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = managerWaitingNumberData
            )
        }

    }

    fun popBackStack() {
        navigator.popBackStack()
    }

    fun repeatNumber() {
        val receiptNumberData =
            managerWaitingNumber.value.currentReceiptNumberData.copy(
                waitingStatus = WaitingStatus.CALLING,
                modifiedDate = Date().time
            )
        viewModelScope.launch {
            usesCases.receiptNumberUseCase(receiptNumberData = receiptNumberData)
        }
    }

    fun nextNumber() {
        viewModelScope.launch {
            if (managerWaitingNumber.value.currentReceiptNumberData.waitingStatus in listOf(WaitingStatus.CALLED, WaitingStatus.CALLING)) {
                val currentReceiptNumberData =
                    managerWaitingNumber.value.currentReceiptNumberData.copy(
                        modifiedDate = Date().time,
                        waitingStatus = WaitingStatus.DONE
                    )
                usesCases.receiptNumberUseCase(receiptNumberData = currentReceiptNumberData)
            }

            if (managerWaitingNumber.value.nextReceiptNumberData.waitingStatus == WaitingStatus.WAIT) {
                val nextReceiptNumberData = managerWaitingNumber.value.nextReceiptNumberData.copy(
                    modifiedDate = Date().time,
                    waitingStatus = WaitingStatus.CALLING
                )
                usesCases.receiptNumberUseCase(receiptNumberData = nextReceiptNumberData)
            }
        }
    }

    fun navSettingScreen() {
        navigator.navigate(Screen.SettingScreen)
    }

    fun clearReceiptNumber() {
        viewModelScope.launch {
            usesCases.initReceiptNumberUseCase()
        }
    }

    init {
        initManagerWaitingNumber()
    }

}