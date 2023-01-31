package kr.pe.paran.waiting.domain.use_cases.setting_data

import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.domain.model.SettingData

class SettingDataUseCase(private val repository: Repository) {

    suspend operator fun invoke(): SettingData {
        return repository.loadSettingData()
    }

    suspend operator fun invoke(settingData: SettingData) {
        repository.saveSettingData(settingData = settingData)
    }
}