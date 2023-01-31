package kr.pe.paran.waiting.data.local.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus

@Dao
interface ReceiptNumberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptNumberData(receiptNumberData: ReceiptNumberData): Long

    @Query("SELECT * FROM receipt_number_table WHERE id = :id")
    suspend fun getReceiptNumberData(id: Int): ReceiptNumberData?

    @Query("SELECT * FROM receipt_number_table WHERE waitingStatus = :waitingStatus ORDER BY createdDate DESC LIMIT 1")
    suspend fun getReceiptNumberData(waitingStatus: WaitingStatus): ReceiptNumberData?

    @Query("SELECT * FROM receipt_number_table WHERE waitingStatus = :waitingStatus ORDER BY modifiedDate ASC LIMIT 1")
    suspend fun getFirstReceiptNumberData(waitingStatus: WaitingStatus = WaitingStatus.WAIT): ReceiptNumberData?

    @Query("SELECT * FROM receipt_number_table WHERE waitingStatus = :waitingStatus ORDER BY createdDate ASC LIMIT :size")
    suspend fun getFirstReceiptNumberList(waitingStatus: WaitingStatus = WaitingStatus.WAIT, size: Int = 2): List<ReceiptNumberData>

    @Query("SELECT * FROM receipt_number_table ORDER BY createdDate DESC LIMIT 1")
    suspend fun getLastReceiptNumberData(): ReceiptNumberData

    @Query("SELECT * FROM receipt_number_table WHERE waitingStatus = :waitingStatus ORDER BY createdDate DESC LIMIT 1")
    suspend fun getLastReceiptNumberData(waitingStatus: WaitingStatus): ReceiptNumberData

    @Query("SELECT IFNULL(MAX(waitingNumber), 0) + 1 FROM receipt_number_table WHERE createdDate >  (SELECT max(createdDate) from receipt_number_table where waitingStatus = 'RESET')")
    suspend fun getLastWaitingNumber(): Int

    @Query(value = "SELECT count(waitingNumber) FROM receipt_number_table WHERE waitingStatus = :waitingStatus")
    fun flowWaitingCount(waitingStatus: WaitingStatus = WaitingStatus.WAIT): Flow<Int>

    @Query(value = "SELECT count(waitingNumber) FROM receipt_number_table WHERE waitingStatus = :waitingStatus")
    suspend fun getWaitingCount(waitingStatus: WaitingStatus = WaitingStatus.WAIT): Int

    @Query("SELECT * FROM receipt_number_table")
    fun getDisplayWaitingList(): Flow<List<ReceiptNumberData>>

    @Query("SELECT * FROM receipt_number_table WHERE waitingStatus = :waitingStatus")
    fun flowCalledWaitingNumber(waitingStatus: WaitingStatus = WaitingStatus.CALLING): Flow<ReceiptNumberData?>

    @Query("SELECT * FROM receipt_number_table WHERE waitingStatus IN (:waitingStatus) ORDER BY modifiedDate DESC")
    fun flowCalledNoneWaitingNumber(waitingStatus: List<WaitingStatus> = listOf(WaitingStatus.CALLING, WaitingStatus.NONE, WaitingStatus.CANCEL)): Flow<ReceiptNumberData?>

    @Query("SELECT * FROM (SELECT * FROM receipt_number_table WHERE createdDate = :calledRegDate UNION SELECT * FROM (SELECT * FROM receipt_number_table WHERE createdDate > :calledRegDate AND waitingStatus = 'WAIT' ORDER BY createdDate ASC LIMIT 2)UNION SELECT * FROM (SELECT * FROM receipt_number_table WHERE createdDate < :calledRegDate  AND waitingStatus = 'DONE' ORDER BY createdDate DESC LIMIT 2)) order by createdDate ASC")
    suspend fun getDisplayWaitingDataList(calledRegDate: Long): List<ReceiptNumberData>

    @Query("SELECT * FROM receipt_number_table WHERE modifiedDate > :regDate AND waitingStatus = :waitingStatus ORDER BY modifiedDate ASC LIMIT 1")
    suspend fun getNextReceiptNumberData(regDate: Long, waitingStatus: WaitingStatus = WaitingStatus.WAIT): ReceiptNumberData

    @Query("SELECT count(waitingNumber) FROM receipt_number_table WHERE waitingStatus = :waitingStatus")
    suspend fun getCountWaitingStatus(waitingStatus: WaitingStatus = WaitingStatus.WAIT): Int

    @Query("UPDATE receipt_number_table set waitingStatus = :toWaitingStatus WHERE waitingStatus = :fromWaitingStatus")
    suspend fun initReceiptNumber(fromWaitingStatus: WaitingStatus = WaitingStatus.WAIT, toWaitingStatus: WaitingStatus = WaitingStatus.CANCEL)

    // UPDATE table SET name='test_name' ORDER BY id DESC LIMIT 1;
    // UPDATE table set col = 1 WHERE id = (SELECT MAX(id) FROM table)
    @Query("UPDATE receipt_number_table set waitingNumber = 0 WHERE createdDate = (SELECT MAX(createdDate) FROM receipt_number_table)")
    suspend fun clearReceiptNumber()


    // CAST((julianday('now') - 2440587.5)*86400000 AS INTEGER)
    @Query("SELECT max(modifiedDate) from receipt_number_table")
    suspend fun flowDetectionChange(): Long

}