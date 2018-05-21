package de.boscall


import android.app.AlertDialog
import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.boscall.dbTasks.RemoveAlarmTask
import de.boscall.dto.Alarm
import de.boscall.util.AlarmStorage
import kotlinx.android.synthetic.main.fragment_alarmlist.*

/**
 * A simple [Fragment] subclass.
 */
class AlarmlistFragment : Fragment() {

    private var listNotes = ArrayList<ListNode>()
    private val alarmAdapter = AlarmAdapter(mutableListOf())

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_alarmlist, container, false)

        return view
    }

    fun initialize() {
        val viewModel = ViewModelProviders.of(activity as FragmentActivity).get(AlarmsViewModel::class.java)
        viewModel.getAlarms().observe(activity as FragmentActivity, object : Observer<MutableList<Alarm>> {
            override fun onChanged(t: MutableList<Alarm>?) {
                alarmAdapter.setAlarms(t!!)
            }
        })
        //AddAlarmsToAdapterTask(alarmAdapter, activity).execute()

        alarmList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        alarmList.layoutManager = LinearLayoutManager(activity)
        alarmList.adapter = alarmAdapter

        val swipeHandler = object : SwipeToDeleteCallback(activity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(activity)
                        .setTitle(R.string.dlg_confirmDelete_title)
                        .setMessage(R.string.dlg_confirmDelete_message)
                        .setIcon(R.drawable.ic_dialog_alert_black_24dp)
                        .setPositiveButton(R.string.dlg_confirmDelete_btnYes, { dialog, which ->
                            // Delete alarm
                            removeAlarm(viewHolder.adapterPosition)
                        })
                        .setNegativeButton(R.string.dlg_confirmDelete_btnNo, { dialog, which ->
                            // Workaround to remove the swipe-effect
                            alarmAdapter.notifyDataSetChanged()
                        }).setCancelable(false)
                        .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(alarmList)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

    private fun removeAlarm(position: Int) {
        val alarmRemoved = alarmAdapter[position]
        alarmAdapter.removeAt(position)
        RemoveAlarmTask(activity).execute(alarmRemoved)
    }

    private fun addAlarm(alarm: Alarm) {
        alarmAdapter.addItem(alarm)
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = sharedPref.edit()
        editor.putString("title", alarm.title)
        editor.putString("content", alarm.text)
        editor.apply()
        AlarmStorage.storeAlarms(activity, alarmAdapter.getList())
    }
}
