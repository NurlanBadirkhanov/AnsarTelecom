package com.richmedia.morningbell

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmDialogActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManagerCompat
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = NotificationManagerCompat.from(this)

        val message = intent.getStringExtra("message")

        // Проверяем разрешение на чтение внешнего хранилища
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Получаем стандартный звуковой сигнал будильника
            val defaultAlarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            // Создаем и запускаем медиаплеер для воспроизведения звука будильника
            mediaPlayer = MediaPlayer.create(this, defaultAlarmUri)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }

        // Показываем неудаляемое уведомление
        showNotification(message)

        // Создаем диалог с будильником
        AlertDialog.Builder(this)
            .setTitle("Время проснуться!")
            .setMessage(message)
            .setPositiveButton("Выключить") { _, _ ->
                mediaPlayer?.stop()
                finish()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
                mediaPlayer?.stop()
                finish()
            }
            .setOnDismissListener {
                mediaPlayer?.stop()
                finish()
            }
            .create()
            .show()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    private fun showNotification(message: String?) {
        val channelId = "alarm_channel"
        val notificationId = 1

        val intent = Intent(this, AlarmDialogActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Будильник воспроизводится")
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Устанавливаем низкий приоритет
            .setAutoCancel(false)

        notificationManager = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alarm Channel", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}
