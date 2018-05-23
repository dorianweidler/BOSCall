package de.boscall.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.boscall.R
import de.boscall.dto.Alarm


class BosCallMessagingService : FirebaseMessagingService() {

    val TAG = this.javaClass.name

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var alarm: Alarm = Alarm(remoteMessage.data.get("title")
                ?: "Kein Alarmtitel", remoteMessage.data.get("text") ?: "Kein Alarmtext")
        AlarmDatabase.getInstance(applicationContext).alarmDao().insert(alarm)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val toneString = sharedPreferences.getString("itmDefaultAlarmtone", "content://media/internal/audio/media/323")
        Log.d(TAG, "Tone: ${toneString}")
        val CHANNEL_ALARM = getString(R.string.not_channel_alarm)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = getString(R.string.not_channel_alarm_name)
            val description = getString(R.string.not_channel_alarm_desc)
            val channel = NotificationChannel(CHANNEL_ALARM, name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = description
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel)
        }

        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ALARM)
                .setSmallIcon(R.drawable.ic_phone_android_black_24dp)
                .setContentTitle(alarm.title)
                .setContentText(alarm.text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Uri.parse(toneString))
        val notification = mBuilder.build()
        notificationManager.notify(0, notification)

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}