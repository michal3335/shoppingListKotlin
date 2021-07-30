package com.example.mapa

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofencingEvent

class GeoReceiver : BroadcastReceiver() {

    var num = System.currentTimeMillis()
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        val triggering = event.triggeringGeofences
        for (geo in triggering) {
            val service = Intent(context, productService::class.java)
            service.putExtra("channel_id",num.toString())
            service.putExtra("channel_name", geo.requestId.toString())
            service.putExtra("shop_id", geo.requestId.toString())
            context.startForegroundService(service)

        }

    }
}