package com.example.cheerpon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

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

        //ボタンの処理
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button_send.setOnClickListener { sendMessage("メッセージを送信") }
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

    //メッセージ送信
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(message: String) {
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var channel = NotificationChannel("new article", "新着記事", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "新着記事を通知するためのチャンネルです"
        channel.enableVibration(true)
        channel.setShowBadge(true)
        manager.createNotificationChannel(channel)

        var builder = NotificationCompat.Builder(this, "new articles")
        var intent  =Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("通知タイトル")
            .setContentText("通知テキスト")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(1, notification)
    }
}
