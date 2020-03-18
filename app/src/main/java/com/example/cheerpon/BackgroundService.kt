package com.example.cheerpon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class BackgroundService : Service(),BackgroundBroadcastReceiverListener {

    //スクリーンロックの処理
    var bbr: BackgroundBroadcastReceiver = BackgroundBroadcastReceiver(this)

    override fun onCreate() {
        super.onCreate()
        //スクリーンロックの処理
        //レシーバーの登録
        registerReceiver(bbr, IntentFilter(Intent.ACTION_SCREEN_ON))
        registerReceiver(bbr, IntentFilter(Intent.ACTION_SCREEN_OFF))
        registerReceiver(bbr, IntentFilter(Intent.ACTION_USER_PRESENT))
    }

    override fun onBind(intent: Intent): IBinder {
        //TODO("Return the communication channel to the service.")
        //とりあえず最初はBindしない
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //サービス開始
        //5秒以内にサービスをユーザに通知する必要がある
        //通知の準備
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "background service"
        val id = "background channel"
        val notifyDescription = "detailed description..."
        if(manager.getNotificationChannel(id) == null){
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }
        //通知
        var pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, id).apply {
            setContentTitle("ちあぽん")
            setContentText("ちあぽん動作中。終了しないでください！")
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_launcher_background)
        }.build()
        //foreground service開始
        startForeground(1, notification)

        //以下、実行する処理
        Log.d("BackgroundService", "onStartCOmman was called.")
        counthundler.post(countrunnable)

        //return super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }



    

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDestroy() {
        super.onDestroy()
        terminating()   //終了処理
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    //終了時にすべき処理
    private fun terminating(){
        //タイマーを止める
        counthundler.removeCallbacks(countrunnable)
        //レシーバーの解除
        unregisterReceiver(bbr)
    }
    /*******************************
     * カウントtimer処理用
     *******************************/
    private var count: Int = 0
    private val counthundler =  Handler()
    private val countrunnable = object: Runnable {
        override fun run() {
            //TODO("Not yet implemented")
            count++;
            Log.d("BackgroundService", count.toString())
            counthundler.postDelayed(this, 1000)
        }
    }

    //BackgroundBroadcastReceiverListener用のメソッド
    override fun onScreenOn() {
        //TODO("Not yet implemented")
        Log.d("BackgroundService", "onScreenOn was called.")
    }

    override fun onScreenOff() {
        //TODO("Not yet implemented")
        Log.d("BackgroundService", "onScreenOff was called.")
    }

    override fun onUserPresent() {
        //TODO("Not yet implemented")
        Log.d("BackgroundService", "onUserPresent was called.")
    }

}
