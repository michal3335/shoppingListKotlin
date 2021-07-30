package com.example.mapa

import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class productService: Service() {

    override fun onCreate() {
        super.onCreate()
       var  chanId = createChannel("channel_Def", "Default_channel")
        val notification = NotificationCompat.Builder(this, chanId)
                .setContentTitle("Serwis wystartowal")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("dzialanie w tle")
                .setAutoCancel(true)
                .build()

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        startForeground(1,notification)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            var chanId = intent!!.getStringExtra("channel_id")
            val chanName = intent.getStringExtra("channel_name")
            if(chanId != null && chanName != null) {
                createChannel(chanId, chanName)
            }else{
                chanId = createChannel("channel_Def", "Default_channel")
            }

            val idShop = intent.getStringExtra("shop_id")

            val id = Random.nextInt(1000)


            val notification = NotificationCompat.Builder(this, chanId)
                    .setContentTitle("Wkroczono na teren sklepu: "+idShop)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText("Kup 2 napoje coca cola w cenie 1")
                    .setAutoCancel(true)
                    .build()

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            with(NotificationManagerCompat.from(this)) {
                notify(id, notification)
            }
       }else{

        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(id: String, name: String): String{
        val notificationChannel = NotificationChannel(
                id,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel)
        return id
    }
}