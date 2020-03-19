package com.example.cheerpon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MyNotificationMessages constructor(private val context: Context) {
    //メッセージ管理用のクラス
    //コンストラクタについての説明は以下。
    //https://teratail.com/questions/180995

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    //メッセージを送るメソッドをとりあえず作ってみる
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(){
        Log.d("MyNotificationMessages", "sendMessage was called")
        //通知チャンネルを作る
        val channel = NotificationChannel("new articles", "新着記事", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "新着記事を通知するためのチャンネルです"
        channel.enableVibration(true)
        channel.setShowBadge(true)
        manager.createNotificationChannel(channel)

        //NotificationCompat.Builderを作る
        val builder  = NotificationCompat.Builder(context, "new article")
        //通知をタップするとひらかれる画面へのインテントをペンディングインテントにつめる
        //val intent = Intent(context, MainActivity::class.java)
        val intent = Intent(context, BackgroundService::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Notificationオブジェクトの作成
        val notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("通知タイトル")
            .setContentText("通知テキスト")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        //通知を行う
        manager.notify(1, notification)
        Log.d("MyNotificationMessages", "sendMessage was dane")
    }

}