package de.boscall


import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        for (pref in sharedPreferences.all.keys) {
            Log.d(this.javaClass.name, "Key ${pref} -> ${(sharedPreferences.all[pref])} -> String? ${(sharedPreferences.all[pref] is String)}")
        }

    }
}// Required empty public constructor
