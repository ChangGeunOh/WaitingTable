package kr.pe.paran.waiting.domain.use_cases.display_waiting

import kotlinx.coroutines.flow.Flow
import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.presentation.screen.display.DisplayStatus

class DisplayWaitingUseCase(
    val repository: Repository
) {
    operator fun invoke(): Flow<DisplayStatus> {
        return repository.flowDisplayStatus()
    }
}