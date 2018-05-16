package de.boscall


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_units.*


/**
 * A simple [Fragment] subclass.
 */
class UnitsFragment : Fragment() {

    private val simpleAdapter = SimpleAdapter((1..5).map { "Item: $it" }.toMutableList())
    private val ZXING_CAMERA_PERMISSION = 1
    private val UNIT_READER_REQUEST = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_units, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initialize()
    }

    fun initialize() {
        // Setup recycler view
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = simpleAdapter

        val swipeHandler = object : SwipeToDeleteCallback(activity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(activity)
                        .setTitle(R.string.dlg_confirmDelete_title)
                        .setMessage(R.string.dlg_confirmDelete_message)
                        .setIcon(R.drawable.ic_dialog_alert_black_24dp)
                        .setPositiveButton(R.string.dlg_confirmDelete_btnYes, DialogInterface.OnClickListener { dialog, which ->
                            // Delete unit
                            val adapter = recyclerView.adapter as SimpleAdapter
                            adapter.removeAt(viewHolder.adapterPosition)
                            // TODO: Call API to remove person, remove from unit list
                        })
                        .setNegativeButton(R.string.dlg_confirmDelete_btnNo, DialogInterface.OnClickListener { dialog, which ->
                            // Workaround to remove the swipe-effect
                            simpleAdapter.notifyDataSetChanged()
                        })
                        .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Setup add-click handler
        btnAddUnit.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.CAMERA), ZXING_CAMERA_PERMISSION)
            } else {
                val intent = Intent(activity, UnitReaderActivity::class.java)
                startActivityForResult(intent, UNIT_READER_REQUEST)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ZXING_CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (this::class != null) {
                        val intent = Intent(activity, UnitReaderActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(activity, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == UNIT_READER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Log.d(javaClass.name, "Unitcode: ${data?.getStringExtra("unit")}")
            }
        }
    }

}// Required empty public constructor
