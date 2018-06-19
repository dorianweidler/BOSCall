package de.boscall.dbTasks

import android.app.Activity
import android.os.AsyncTask
import de.boscall.dto.Alarm
import de.boscall.services.AlarmDatabase
import java.lang.ref.WeakReference

class RemoveAllAlarmsTask(activity: Activity) : AsyncTask<Unit, Unit, Unit>() {

    val activity: WeakReference<Activity> = WeakReference(activity)

    override fun doInBackground(vararg params: Unit) {
        val activity = this.activity.get()
        if (activity != null) {
            val db = AlarmDatabase.getInstance(activity)

            db.alarmDao().deleteAll()
        }
    }
}