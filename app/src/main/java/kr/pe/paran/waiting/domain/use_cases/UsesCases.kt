package kr.pe.paran.waiting.domain.use_cases

import kr.pe.paran.waiting.domain.use_cases.display_images.DisplayImagesUseCase
import kr.pe.paran.waiting.domain.use_cases.display_waiting.DisplayWaitingUseCase
import kr.pe.paran.waiting.domain.use_cases.init_receipt.InitReceiptNumberUseCase
import kr.pe.paran.waiting.domain.use_cases.manager_waiting.ManagerWaitingUseCase
import kr.pe.paran.waiting.domain.use_cases.mode_type.ModeTypeUseCase
import kr.pe.paran.waiting.domain.use_cases.number_pad.NumberPadUseCase
import kr.pe.paran.waiting.domain.use_cases.receipt_number.ReceiptNumberUseCase
import kr.pe.paran.waiting.domain.use_cases.screen_mode.ScreenModeUseCase
import kr.pe.paran.waiting.domain.use_cases.waiting_count.WaitingCountUseCase
import kr.pe.paran.waiting.domain.use_cases.server_ip.ServerIpUseCase
import kr.pe.paran.waiting.domain.use_cases.setting_data.SettingDataUseCase

data class UsesCases(
    val receiptNumberUseCase: ReceiptNumberUseCase,
    val waitingCountUseCase: WaitingCountUseCase,
    val serverIpUseCase: ServerIpUseCase,
    val modeTypeUseCase: ModeTypeUseCase,
    val screenModeUseCase: ScreenModeUseCase,
    val displayWaitingUseCase: DisplayWaitingUseCase,
    val managerWaitingUseCase: ManagerWaitingUseCase,
    val initReceiptNumberUseCase: InitReceiptNumberUseCase,
    val settingDataUseCase: SettingDataUseCase,
    val numberPadUseCase: NumberPadUseCase,
    val displayImagesUseCase: DisplayImagesUseCase
)
