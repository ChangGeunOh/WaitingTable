package kr.pe.paran.waiting.domain.use_cases.manager_waiting

import kotlinx.coroutines.flow.Flow
import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.presentation.screen.manager.contents.ManagerWaitingNumber

class ManagerWaitingUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<ManagerWaitingNumber> {
        return repository.flowManagerWaiting()
    }
}