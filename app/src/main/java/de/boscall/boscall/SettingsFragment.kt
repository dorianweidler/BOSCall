package de.boscall.boscall


import android.app.Fragment
import android.os.Bundle
import android.preference.PreferenceFragment


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
    }
}// Required empty public constructor
