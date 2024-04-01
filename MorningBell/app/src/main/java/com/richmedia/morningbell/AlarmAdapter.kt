package com.richmedia.morningbell

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class AlarmAdapter(private val alarmList: List<AlarmModel>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        val timeTextView = holder.itemView.findViewById<TextView>(R.id.alarmTimeTextView)
        timeTextView.text = getTimeString(alarm.timeInMillis)

        val switch = holder.itemView.findViewById<SwitchCompat>(R.id.toggleAlarmButton)
        switch.isChecked = alarm.isActive

        switch.setOnCheckedChangeListener { _, isChecked ->
            alarm.isActive = isChecked
            if (!isChecked) {
                // Здесь вы можете добавить код для деактивации будильника
                // Например, отменить срабатывание будильника или выполнить другие действия
            }
        }
    }


    private fun getTimeString(timeInMillis: Long): String {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }


    override fun getItemCount(): Int {
        return alarmList.size
    }

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Инициализация представлений из макета элемента item_alarm
    }
}
