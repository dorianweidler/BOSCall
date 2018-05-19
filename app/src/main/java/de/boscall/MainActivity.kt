package de.boscall

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item -> switchFragment(item.itemId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (fragmentManager.findFragmentById(R.id.replaceFrame) == null) {
            switchFragment(R.id.nav_alarmlist)
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val token = FirebaseInstanceId.getInstance().token
        Log.d(this.javaClass.name, "FCM Registration Token: " + token)
    }

    private fun switchFragment(navId: Int): Boolean {
        val fragment: Fragment?

        when (navId) {
            R.id.nav_alarmlist -> {
                fragment = AlarmlistFragment()
            }
            R.id.nav_news -> {
                fragment = NewsFragment()
            }
            R.id.nav_settings -> {
                fragment = SettingsFragment()
            }
            R.id.nav_units -> {
                fragment = UnitsFragment()
            }
            else -> {
                return false
            }
        }

        fragmentManager.beginTransaction()
                .replace(R.id.replaceFrame, fragment)
                .commit()

        return true
    }

    fun setContentByNavId(navId: Int) {
        navigation.selectedItemId = navId
    }

    enum class FragmentID {
        ALARMLIST, NEWS, UNITS, SETTINGS
    }
}
