package kr.pe.paran.waiting.domain.use_cases.number_pad

import kotlinx.coroutines.flow.Flow
import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData

class NumberPadUseCase(
    private val repository: Repository
) {
    operator fun invoke(): Flow<NumberPadData> {
        return repository.flowNumberPadData()
    }
}