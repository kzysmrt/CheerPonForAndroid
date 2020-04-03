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

class BackgroundService: Service(), BackgroundBroadcastReceiverListener {

    //コンテキストの処理
    /*
    private lateinit var context: Context
    private lateinit var mnm: MyNotificationMessages
    init{
        context = _context
        mnm = MyNotificationMessages(context)
    }
    */
    //var mnm = MyNotificationMessages(this)

    //時間処理
    val mtcc: MyTimeCalculationClass = MyTimeCalculationClass()

    //スクリーンロックの処理
    var bbr: BackgroundBroadcastReceiver = BackgroundBroadcastReceiver(this)

    override fun onCreate() {
        super.onCreate()
        //コンテキストの処理

        //現在時刻の表示
        Log.d("time", mtcc.getStartTime())
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
        //通知チャネルの作成
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
     * 1秒ごとにする処理
     *******************************/
    private var count: Int = 0
    private val counthundler =  Handler()
    private val countrunnable = object: Runnable {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            //TODO("Not yet implemented")
            count++
            Log.d("BackgroundService", count.toString())
            //
            Log.d("time", mtcc.getNowTime())
            Log.d("time", mtcc.getNowUnixTime())

            //5病後に通知をする
            if(count == 5){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   sendMessage("バックグラウンドの通知です")
                }
            }

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


    //メッセージの処理
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(message: String) {
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var channel = NotificationChannel("new article for background service", "BG新着記事", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "BG新着記事を通知するためのチャンネルです"
        channel.enableVibration(true)
        channel.setShowBadge(true)
        manager.createNotificationChannel(channel)

        var builder = NotificationCompat.Builder(this, "new articles")
        var intent  =Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("BG通知タイトル")
            .setContentText("BG通知テキスト: " + message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(1, notification)
    }
}
