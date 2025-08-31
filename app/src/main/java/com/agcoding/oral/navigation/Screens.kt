package com.agcoding.oral.navigation


enum class Screens(val route: String) {
    Home("home"),
    Capture("capture"),
    EndSession("end_session"),
    Search("search"),
    SessionDetail("session_detail/{sessionId}")
}