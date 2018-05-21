package de.boscall

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import de.boscall.dto.Alarm
import de.boscall.services.AlarmDatabase

class AlarmsViewModel : AndroidViewModel {
    private val alarms: LiveData<MutableList<Alarm>>

    constructor(application: Application) : super(application) {
        alarms = AlarmDatabase.getInstance(getApplication()).alarmDao().getAll()
    }

    fun getAlarms(): LiveData<MutableList<Alarm>> {
        return alarms
    }

}
