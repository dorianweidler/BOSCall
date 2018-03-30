package de.boscall.boscall

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
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

        val manager: FragmentManager = fragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.ReplaceFrame)
        if (fragment == null) {
            fragment = AlarmlistFragment()
            manager.beginTransaction().add(R.id.ReplaceFrame, fragment).commit()
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

        if (fragment != null) { // in case code is extended and not properly modified
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.ReplaceFrame, fragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }
    }

    enum class FragmentID {
        ALARMLIST, NEWS, SETTINGS
    }
}
