package kr.pe.paran.waiting.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.domain.use_cases.UsesCases
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUseCase(
        repository: Repository
    ): UsesCases {
        return UsesCases(
            receiptNumberUseCase = ReceiptNumberUseCase(repository = repository),
            waitingCountUseCase = WaitingCountUseCase(repository = repository),
            serverIpUseCase = ServerIpUseCase(repository = repository),
            modeTypeUseCase = ModeTypeUseCase(repository = repository),
            screenModeUseCase = ScreenModeUseCase(repository = repository),
            displayWaitingUseCase = DisplayWaitingUseCase(repository = repository),
            managerWaitingUseCase = ManagerWaitingUseCase(repository = repository),
            initReceiptNumberUseCase = InitReceiptNumberUseCase(repository = repository),
            settingDataUseCase = SettingDataUseCase(repository = repository),
            numberPadUseCase = NumberPadUseCase(repository = repository),
            displayImagesUseCase = DisplayImagesUseCase(repository = repository)
        )
    }
}