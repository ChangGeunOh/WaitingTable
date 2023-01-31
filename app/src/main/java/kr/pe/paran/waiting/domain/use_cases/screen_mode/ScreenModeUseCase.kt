package kr.pe.paran.waiting.domain.use_cases.screen_mode

import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.domain.model.ScreenMode

class ScreenModeUseCase(private val repository: Repository) {

    suspend operator fun invoke(): ScreenMode {
        return repository.loadScreenMode()
    }

    suspend operator fun invoke(screenMode: ScreenMode) {
        repository.saveScreenMode(screenMode = screenMode)
    }
}