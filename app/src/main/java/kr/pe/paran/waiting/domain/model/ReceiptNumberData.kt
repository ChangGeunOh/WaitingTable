package kr.pe.paran.waiting.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@kotlinx.serialization.Serializable
@Entity(tableName = "receipt_number_table")
data class ReceiptNumberData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String = "",
    var waitingNumber: Int = 0,
    var waitingStatus: WaitingStatus = WaitingStatus.NONE,
    val createdDate: Long = Date().time,
    val modifiedDate: Long = Date().time
)