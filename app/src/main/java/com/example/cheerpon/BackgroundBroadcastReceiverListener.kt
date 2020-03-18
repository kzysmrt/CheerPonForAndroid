package com.example.cheerpon

interface BackgroundBroadcastReceiverListener {
    fun onScreenOn()
    fun onScreenOff()
    fun onUserPresent()
}