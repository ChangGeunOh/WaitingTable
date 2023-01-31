package kr.pe.paran.waiting.domain.use_cases.mode_type

import kr.pe.paran.waiting.data.repository.Repository

class ModeTypeUseCase(val repository: Repository) {

    suspend operator fun invoke(): Int {
        return repository.loadModeType()
    }

    suspend operator fun invoke(modeType: Int) {
        repository.saveModeType(modeType = modeType)
    }

}