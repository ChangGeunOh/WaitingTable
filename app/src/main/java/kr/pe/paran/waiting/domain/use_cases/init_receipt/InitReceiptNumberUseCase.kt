package kr.pe.paran.waiting.domain.use_cases.init_receipt

import kr.pe.paran.waiting.data.repository.Repository

class InitReceiptNumberUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke() {
        repository.initReceiptNumber()
    }
}