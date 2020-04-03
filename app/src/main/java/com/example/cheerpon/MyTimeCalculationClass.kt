package com.example.cheerpon

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MyTimeCalculationClass {
    //時間計算などデータ処理を行うクラス

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @RequiresApi(Build.VERSION_CODES.O)
    var starttime = LocalDateTime.now().format(formatter)

    fun getStartTime(): String{
        return starttime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowTime(): String{
        val nowtime = LocalDateTime.now()
        return nowtime.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowUnixTime(): String{
        return changeLocalDateToUnixTime(LocalDateTime.now()).toString()
    }

    //Unix timeに変換
    //https://blogenist.jp/2019/01/30/7571/
    @RequiresApi(Build.VERSION_CODES.O)
    fun changeLocalDateToUnixTime(localdatetime: LocalDateTime): Long {
        val zonedDateTime = localdatetime.atZone(ZoneOffset.ofHours(+9))
        val unixTime = zonedDateTime.toInstant().toEpochMilli()
        return unixTime
    }

}