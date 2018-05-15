package de.boscall

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_alarmlist -> {
                switchFragment(FragmentID.ALARMLIST)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_news -> {
                switchFragment(FragmentID.NEWS)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_settings -> {
                switchFragment(FragmentID.SETTINGS)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (fragmentManager.findFragmentById(R.id.replaceFrame) == null) {
            switchFragment(FragmentID.ALARMLIST)
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val token = FirebaseInstanceId.getInstance().token
        Log.d(this.javaClass.name, "FCM Registration Token: " + token)
    }

    fun switchFragment(fragmentId: FragmentID) {
        val fragment: Fragment?

        when (fragmentId) {
            FragmentID.ALARMLIST -> {
                fragment = AlarmlistFragment()
            }
            FragmentID.NEWS -> {
                fragment = NewsFragment()
            }
            FragmentID.SETTINGS -> {
                fragment = SettingsFragment()
            }
        }

        fragmentManager.beginTransaction()
                .replace(R.id.replaceFrame, fragment)
                .commit()

        /*if (fragment != null) { // in case code is extended and not properly modified

        }*/
    }

    enum class FragmentID {
        ALARMLIST, NEWS, SETTINGS
    }
}
