package com.richmedia.morningbell




import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("key")
        Log.d("MyLog", "Alarm message: $message")

        // Создаем новое Intent для запуска активити, которая будет отображать диалог
        val dialogIntent = Intent(context, AlarmDialogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        // Передаем сообщение диалогу через Intent
        dialogIntent.putExtra("message", message)
        context.startActivity(dialogIntent)
    }
}


