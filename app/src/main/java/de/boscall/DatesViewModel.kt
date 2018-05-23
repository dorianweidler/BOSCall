package de.boscall

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import de.boscall.dto.Date
import de.boscall.services.DateDatabase

class DatesViewModel : AndroidViewModel {
    private val dates: LiveData<MutableList<Date>>

    constructor(application: Application) : super(application) {
        dates = DateDatabase.getInstance(getApplication()).dateDao().getAll()
    }

    fun getDates(): LiveData<MutableList<Date>> {
        return dates
    }

}
