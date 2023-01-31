package kr.pe.paran.waiting.common.utils

import android.util.Log

object Logcat  {

    fun v(msg: String) {
        Log.v(tag(), msg)
    }

    fun d(msg: String) {
        Log.d(tag(), msg)
    }

    fun i(msg: String) {
        Log.i(tag(), msg)
    }

    fun w(msg: String) {
        Log.w(tag(), msg)
    }

    fun e(msg: String) {
        Log.e(tag(), msg)
    }

    fun info(msg: String) {
        Log.i(tag(), msg)
    }

    private fun tag(): String {
        val trace = Thread.currentThread().stackTrace[4]
        val fileName = trace.fileName
        val lineNumber = trace.lineNumber
        return ":::$fileName:$lineNumber>"
    }
}