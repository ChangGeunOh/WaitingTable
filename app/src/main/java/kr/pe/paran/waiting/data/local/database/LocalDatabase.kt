package kr.pe.paran.waiting.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.data.local.database.dao.ReceiptNumberDao
import kr.pe.paran.waiting.domain.model.ReceiptNumberData

@Database(entities = [ReceiptNumberData::class], version = Constants.DATABASE_VERSION, exportSchema = false)
@TypeConverters(DatabaseConverter::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun receiptDao(): ReceiptNumberDao
}