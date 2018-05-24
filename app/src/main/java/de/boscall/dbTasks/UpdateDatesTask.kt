package de.boscall.dbTasks

import android.app.Activity
import android.os.AsyncTask
import com.google.gson.JsonObject
import de.boscall.dto.Date
import de.boscall.services.DateDatabase
import retrofit2.Call
import java.lang.ref.WeakReference


class UpdateDatesTask(activity: Activity) : AsyncTask<Call<List<JsonObject>>, Date, Boolean>() {

    val activity: WeakReference<Activity> = WeakReference(activity)

    override fun doInBackground(vararg params: Call<List<JsonObject>>): Boolean? {
        val activity = this.activity.get()
        if (activity != null) {
            val db = DateDatabase.getInstance(activity)
            for (call in params) {
                val response = call.execute()
                if (response.body() != null) {
                    for (o in response.body()!!) {
                        db.dateDao().insert(dateFromJSON(o))
                    }
                } else{
                    return false
                }
            }
            return true
        }
        return false
    }

    private fun dateFromJSON(o : JsonObject): Date {
        if (o.has("date") && o.has("title") && o.has("description")) {
            return Date( o.get("title").asString, o.get("description").asString, o.get("date").asString)
        } else{
           return Date("", "", "")
        }
    }
}