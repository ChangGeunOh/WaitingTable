package kr.pe.paran.waiting.domain.use_cases.receipt_number

import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.domain.model.WaitingStatus

class ReceiptNumberUseCase(private val repository: Repository) {

    suspend operator fun invoke(): Int {
        return repository.loadLastNumber()
    }

    suspend operator fun invoke(id: Int) {
        repository.loadReceiptNumberData(id = id)
    }

    suspend operator fun invoke(isFirst: Boolean): ReceiptNumberData{
        return repository.loadReceiptNumberData(isFirst = isFirst)
    }

    suspend operator fun invoke(waitingStatus: WaitingStatus): ReceiptNumberData {
        return repository.loadReceiptNumberData(waitingStatus = waitingStatus)
    }

    suspend operator fun invoke(receiptNumberData: ReceiptNumberData): String {
        return repository.saveReceiptNumberData(receiptNumberData = receiptNumberData)
    }

}
