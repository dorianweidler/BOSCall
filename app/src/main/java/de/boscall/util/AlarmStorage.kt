package de.boscall.util

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.boscall.dto.Alarm
import java.io.File
import java.io.PrintWriter
import java.util.*

class AlarmStorage {
    companion object {
        private val ALARMS_FILENAME = "alarms.json"

        fun readAlarmsFromFile(context: Context): MutableList<Alarm> {
            val alarmType = object : TypeToken<MutableList<Alarm>>() {}.type
            val gson = GsonBuilder().create()
            val alarmsStorage = File(context.filesDir, ALARMS_FILENAME)
            var alarms = mutableListOf<Alarm>()
            if (alarmsStorage.exists()) {
                val reader = Scanner(alarmsStorage)
                if (reader.hasNextLine()) {
                    val obj = reader.nextLine()
                    reader.close()
                    if (obj != null) {
                        alarms = gson.fromJson(obj, alarmType)
                    }
                }
            } else {
                alarmsStorage.createNewFile()
            }
            return alarms
        }

        fun storeAlarms(context: Context, alarms: MutableList<Alarm>) {
            val alarmStorage = File(context.filesDir, ALARMS_FILENAME)
            val gson = GsonBuilder().create()
            val alarmType = object : TypeToken<MutableList<Alarm>>() {}.type
            val alarmJson = gson.toJson(alarms, alarmType)
            if (!alarmStorage.exists()) {
                alarmStorage.createNewFile()
            }
            val writer = PrintWriter(alarmStorage)
            writer.print(alarmJson)
            writer.flush()
        }
    }


}