package de.boscall.util

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.boscall.dto.Registration
import java.io.File
import java.io.PrintWriter
import java.util.*

class RegistrationStorage {
    companion object {
        private val REGISTRATIONS_FILENAME = "registrations.json"
        fun readRegistrationsFromFile(context: Context): MutableList<Registration> {
            val registrationType = object : TypeToken<MutableList<Registration>>() {}.type
            val gson = GsonBuilder().create()
            val unitStorage = File(context.filesDir, REGISTRATIONS_FILENAME)
            var units = mutableListOf<Registration>()
            if (unitStorage.exists()) {
                val reader = Scanner(unitStorage)
                if (reader.hasNextLine()) {
                    val obj = reader.nextLine()
                    reader.close()
                    if (obj != null) {
                        units = gson.fromJson(obj, registrationType)
                    }
                }

            } else {
                unitStorage.createNewFile()
            }
            return units
        }

        fun storeRegistrations(context: Context, registrations: MutableList<Registration>) {
            val registrationStorage = File(context.filesDir, REGISTRATIONS_FILENAME)
            val gson = GsonBuilder().create()
            val registrationType = object : TypeToken<MutableList<Registration>>() {}.type
            val registrationJson = gson.toJson(registrations, registrationType)
            if (!registrationStorage.exists()) {
                registrationStorage.createNewFile()
            }
            val writer = PrintWriter(registrationStorage)
            writer.print(registrationJson)
            writer.flush()
        }
    }


}