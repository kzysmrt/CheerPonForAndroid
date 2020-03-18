package com.example.cheerpon

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Foreground Serviceを起動
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d("Main thread", "start foreground service...")
            start()
        }else{
            Toast.makeText(this, "Androidのバージョンを最新にしてください。", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //サービスを停止
        val intent = Intent(this, BackgroundService::class.java)
        stopService(intent)
    }

    /***** 以下、独自メソッド *****/
    //forefround serviceを起動
    @RequiresApi(Build.VERSION_CODES.O)
    private fun start(){
        val serviceIntent = Intent(this, BackgroundService::class.java)
        startForegroundService(serviceIntent)
    }
}
