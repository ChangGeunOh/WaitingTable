package kr.pe.paran.waiting.data.local.database

import androidx.room.TypeConverter
import kr.pe.paran.waiting.domain.model.WaitingStatus

class DatabaseConverter {

    private val separator = ","

    @TypeConverter
    fun convertListToString(list: List<String>): String {
        val stringBuilder = StringBuilder()
        for (item in list) {
            stringBuilder.append(item).append(separator)
        }
        stringBuilder.setLength(stringBuilder.length - separator.length)
        return stringBuilder.toString()
    }

    @TypeConverter
    fun convertStringToList(string: String): List<String> {
        return string.split(separator)
    }

    @TypeConverter
    fun fromWaitingStatus(waitingStatus: WaitingStatus) = waitingStatus.name

    @TypeConverter
    fun toWaitingStatus(name: String) = WaitingStatus.valueOf(name)
}