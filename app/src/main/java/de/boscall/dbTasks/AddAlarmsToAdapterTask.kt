package de.boscall.dbTasks

import android.app.Activity
import android.os.AsyncTask
import de.boscall.AlarmAdapter
import de.boscall.dto.Alarm
import de.boscall.services.AlarmDatabase
import java.lang.ref.WeakReference

class AddAlarmsToAdapterTask(alarmAdapter: AlarmAdapter, activity: Activity) : AsyncTask<Unit, Unit, List<Alarm>>() {

    val alarmAdapter: AlarmAdapter = alarmAdapter
    val activity: WeakReference<Activity> = WeakReference(activity)

    override fun doInBackground(vararg params: Unit?): List<Alarm> {
        val activity = this.activity.get()
        if (activity == null) {
            return listOf()
        }
        val db = AlarmDatabase.getInstance(activity)
        val alarmDao = db?.alarmDao()

        return alarmDao?.getAll()!!
    }

    override fun onPostExecute(result: List<Alarm>?) {
        for (alarm in result!!) {
            alarmAdapter.addItem(alarm)
        }
    }
}