package com.richmedia.morningbell

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.richmedia.morningbell.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val calendar = Calendar.getInstance()
    private lateinit var alarmIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager
    private val alarmList = mutableListOf<AlarmModel>()
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var alarmSharedPreferences: AlarmSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.etSetTime.setIs24HourView(true)
        notificationManager = NotificationManagerCompat.from(this)
        alarmSharedPreferences = AlarmSharedPreferences(this)
        alarmList.addAll(alarmSharedPreferences.getAlarms())

        setContentView(binding.root)

        binding.etSetTime.apply {
            setBackgroundColor(Color.WHITE)

        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dhf)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setStatusBarIconColorToWhite(this)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmAdapter = AlarmAdapter(alarmList)
        binding.rc.adapter = alarmAdapter
        binding.rc.layoutManager = LinearLayoutManager(this)

        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("key", "Просыпайся!")
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        binding.btnSavetime.setOnClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, binding.etSetTime.hour)
            calendar.set(Calendar.MINUTE, binding.etSetTime.minute)

            val alarmModel = AlarmModel(calendar.timeInMillis, isActive = true)
            alarmList.add(alarmModel)
            alarmAdapter.notifyItemInserted(alarmList.size - 1) // Добавить эту строку
            alarmAdapter.notifyDataSetChanged() // Добавить эту строку

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )

            // Сохраняем список будильников
            alarmSharedPreferences.saveAlarms(alarmList)

            // Показываем уведомление о разрешении после установки будильника
            showConfirmationNotification()
        }

    }

    // Этот метод изменяет цвет значков статус-бара на белый
    fun changeStatusBarIconColorToWhite(window: Window) {
        // Проверяем версию Android, так как способы изменения цвета значков различаются
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Используем флаг SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, чтобы сделать значки статус-бара светлыми (белыми)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Используем флаг Window.FEATURE_ACTION_BAR_OVERLAY, чтобы разрешить прозрачность статус-бара
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // Устанавливаем цвет статус-бара на белый
            window.statusBarColor = Color.WHITE
        }
    }

    // Используйте этот метод в вашей активности или фрагменте для изменения цвета значков статус-бара на белый
    fun setStatusBarIconColorToWhite(activity: Activity) {
        val window = activity.window
        changeStatusBarIconColorToWhite(window)
    }


    private fun showConfirmationNotification() {
        val channelId = "confirmation_channel"
        val notificationId = 2

        // Используйте свой собственный ресурс изображения вместо R.drawable.icon
        val notificationIcon = R.drawable.icon

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Разрешение получено")
            .setContentText("Теперь вы будете получать уведомления о будильниках")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(notificationIcon)


        notificationManager = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Confirmation Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION_CODE
            )
        } else {
            // Если разрешение уже предоставлено, показываем уведомление
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }


    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION_CODE = 1001
    }
}
