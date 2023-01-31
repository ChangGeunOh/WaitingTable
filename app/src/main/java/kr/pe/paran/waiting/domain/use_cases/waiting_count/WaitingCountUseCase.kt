package kr.pe.paran.waiting.domain.use_cases.waiting_count

import kotlinx.coroutines.flow.Flow
import kr.pe.paran.waiting.data.repository.Repository

class WaitingCountUseCase (
    private val repository: Repository
) {
    operator fun invoke(): Flow<Int> {
        return repository.flowWaitingCount()
    }
}