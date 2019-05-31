package com.es.evampsocial.Services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.es.evampsocial.MainView.chat.ChatActivity
import com.es.evampsocial.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FirebaseMessaging"
    override fun onNewToken(token: String?) {

        Log.d(TAG, "Refreshed Token :$token")
        super.onNewToken(token)
    }

    /* override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage!!.notification!= null){
            Log.d("FCM",remoteMessage.data.toString())
        }
    }
}*/

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        Log.d("Msg", "Message received [$remoteMessage]")

        if (remoteMessage!!.notification != null) {
            Log.d("FCM", remoteMessage.data.toString())

            // Create Notification
            val intent = Intent(this, ChatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 1,
                    intent, PendingIntent.FLAG_ONE_SHOT)

            val notificationBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                    .setContentTitle("Message")
                    .setContentText(remoteMessage.notification!!.body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(1410, notificationBuilder.build())
        }
    }
}

/*

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            Log.d("FCM ", "FCM message Received")
            SendNotification(remoteMessage.notification!!.body)
        }

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val NOTIFICATION_CHANNEL_ID= " FCM ID"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel=NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Your Notifications",NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.description="Description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor=Color.RED
            notificationChannel.vibrationPattern= longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel=notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }
        val notificationBuilder=NotificationCompat.Builder( this,NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this,R.color.colorAccent))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage!!.notification!!.body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                .setAutoCancel(true)

        notificationManager.notify(1000,notificationBuilder.build())
    }

    private fun SendNotification(body: String?) {
        val intent=Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var notificationSound:Uri?=null
        notificationSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder=NotificationCompat.Builder( this)
        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this,R.color.colorAccent))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                .setAutoCancel(true)
        notificationManager.notify(1000,notificationBuilder.build())

    }

}*/