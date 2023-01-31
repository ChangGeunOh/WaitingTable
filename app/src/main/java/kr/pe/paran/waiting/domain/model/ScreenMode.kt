package kr.pe.paran.waiting.domain.model

enum class ScreenMode(val korean: String) {
    NONE("설정 화면 없음"),
    WAITING("대기 접수 화면"),
    MANAGER("대기 관리 화면"),
    DISPLAY("대기 현황 화면")
}