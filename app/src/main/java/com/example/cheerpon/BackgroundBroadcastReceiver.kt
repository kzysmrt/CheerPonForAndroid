package com.example.cheerpon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BackgroundBroadcastReceiver constructor(_listener: BackgroundBroadcastReceiverListener): BroadcastReceiver() {

    private lateinit var listener: BackgroundBroadcastReceiverListener

    init{
        listener = _listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //TODO("backgroundBroadcastReceiver.onReceive() is not implemented")
        var action: String? = intent.getAction()
        if(action.equals(Intent.ACTION_SCREEN_ON)){
            Log.d("BroadcastReceiver", "ACTION_SCREEN_ON")
            listener.onScreenOn()

        }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
            Log.d("BroadcastReceiver", "ACTION_SCREEN_OFF")
            listener.onScreenOff()

        }else if(action.equals(Intent.ACTION_USER_PRESENT)){
            Log.d("BackgroundReceiver", "ACTION_USER_PRESENT")
            listener.onUserPresent()

        }
    }


}
