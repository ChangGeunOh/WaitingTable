package kr.pe.paran.waiting.domain.model

enum class WaitingStatus {
    NONE,
    WAIT,
    CALLING,
    CALLED,
    RESET,
    DONE,
    CANCEL
}