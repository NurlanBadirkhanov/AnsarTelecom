package com.richmedia.morningbell

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmSharedPreferences(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "com.richmedia.morningbell.ALARMS",
        Context.MODE_PRIVATE
    )

    fun saveAlarms(alarms: List<AlarmModel>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(alarms)
        editor.putString("ALARMS", json)
        editor.apply()
    }

    fun getAlarms(): List<AlarmModel> {
        val gson = Gson()
        val json = sharedPreferences.getString("ALARMS", "")
        val type = object : TypeToken<List<AlarmModel>>() {}.type
        return gson.fromJson<List<AlarmModel>>(json, type) ?: listOf()
    }
}

