package kr.pe.paran.waiting.common.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object Utils {

    // 오늘날짜 가져오기
    fun getToday(pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.KOREA)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        return sdf.format(Calendar.getInstance().time)
    }

    fun getDate(value: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREAN)
        return formatter.parse(value) ?: Date()
    }

    fun differenceDate(date1: Date, date2: Date = Date()): String {

        val diffInMilliSec = date2.time - date1.time
        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(diffInMilliSec)
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSec)
        val diffInMin: Long = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec)
        val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diffInMilliSec)

        return when {
            diffInDays > 0 -> "${diffInDays}일 전"
            diffInHours > 0 -> "${diffInHours}시간 전"
            diffInMin > 0 -> "${diffInMin}분 전"
            else -> "${diffInSec}초 전"
        }
    }

    fun getRegDate(text: String): String {

        val digit = text.replace(regex = "[^0-9]".toRegex(), "").toInt()
        val differentTime = when {
            text.contains("초") -> digit
            text.contains("분") -> 60 * digit
            text.contains("시") -> 60 * 60 * digit
            text.contains("일") -> 3600 * 24 * digit
            else -> 365 * 24 * 3600 * digit
        }

        return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(Date().time - (differentTime.toLong() * 1000L)))
    }

    fun convertQRCodeToBitmap(qrCode: String): Bitmap {

        val qrCodeWrite = QRCodeWriter()
        val bitMatrix = qrCodeWrite.encode(qrCode, BarcodeFormat.QR_CODE, 800, 800)

        val bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
        for (x in 0 until bitMatrix.width) {
            for (y in 0 until bitMatrix.height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix.get(
                            x,
                            y
                        )
                    ) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                )
            }
        }
        return bitmap
    }


    fun showDialogPermission(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("권한설정")
            .setMessage("원할한 서비스 제공을 위해 App 설정에서 권한을 설정해 주세요.")
            .setPositiveButton("설정") { dialog, _ ->
                showApplicationSettings(context)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun showApplicationSettings(context: Context) {
        val appDetail = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + context.packageName)
        )
        appDetail.addCategory(Intent.CATEGORY_DEFAULT)
        appDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(appDetail)
    }


}

// SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(item.update_time * 1000L))
fun showMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Context.getActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}