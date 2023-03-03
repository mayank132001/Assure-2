package com.app.effistay.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.effistay.R
import com.app.effistay.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)

        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
        }

        val message = remoteMessage.data["message"]
        val imageUri = remoteMessage.data["image"]
        val TrueOrFlase = remoteMessage.data["AnotherActivity"]
        val notification = remoteMessage.notification
        val data = remoteMessage.data

        sendNotification(notification!!, data)
    }

     fun sendNotification(
        notification: RemoteMessage.Notification,
        data: Map<String, String>
    ) {
        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(notification.title)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
        try {
            val picture_url = data["picture_url"]
            if (picture_url != null && "" != picture_url) {
                val url = URL(picture_url)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
                        .setSummaryText(notification.body)
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
    companion object {
        private const val TAG = "FirebaseMessageService"
    }
}
