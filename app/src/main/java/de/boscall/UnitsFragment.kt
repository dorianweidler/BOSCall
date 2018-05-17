package de.boscall


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.boscall.constants.ServiceConfiguration
import de.boscall.dto.Registration
import de.boscall.dto.RegistrationRequest
import de.boscall.dto.UnregistrationRequest
import de.boscall.services.BosCallWebAPIService
import kotlinx.android.synthetic.main.fragment_units.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.PrintWriter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UnitsFragment : Fragment() {
    private val REGISTRATIONS_FILENAME = "registrations.json"
    private val simpleAdapter = SimpleRegistrationAdapter(mutableListOf())
    private val ZXING_CAMERA_PERMISSION = 1
    private val UNIT_READER_REQUEST = 1
    private val SERVICE = Retrofit.Builder().baseUrl(ServiceConfiguration.API_ADDRESS).addConverterFactory(GsonConverterFactory.create()).build().create(BosCallWebAPIService::class.java)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_units, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        // Load units
        val registrations = readRegistrationsFromFile()
        Log.d(javaClass.name, "List: ${registrations.size}")

        for (registration in registrations) {
            simpleAdapter.addItem(registration)

        }
        simpleAdapter.notifyDataSetChanged()

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
                        .setPositiveButton(R.string.dlg_confirmDelete_btnYes, { dialog, which ->
                            // Delete unit
                            removeRegistration(viewHolder.adapterPosition)
                        })
                        .setNegativeButton(R.string.dlg_confirmDelete_btnNo, { dialog, which ->
                            // Workaround to remove the swipe-effect
                            simpleAdapter.notifyDataSetChanged()
                        }).setCancelable(false)
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
                    val intent = Intent(activity, UnitReaderActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, getString(R.string.unitReader_toast_permissionRequest_failed), Toast.LENGTH_SHORT).show()
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
                val registrationData = JSONObject(data?.getStringExtra("unit"))
                Log.d(javaClass.name, "= Registration data =: ${registrationData}")
                val request = RegistrationRequest(registrationData.getLong("id"), registrationData.getString("secret"), FirebaseInstanceId.getInstance().token
                        ?: "")
                val call = SERVICE.registerUnit(request)
                call.enqueue(object : Callback<Registration> {
                    override fun onFailure(call: Call<Registration>?, t: Throwable?) {
                        Log.d(javaClass.name, "Service call failed")
                        Toast.makeText(activity, getString(R.string.unitReader_toast_serviceCall_failed), Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<Registration>?, response: Response<Registration>) {
                        if (response.code() == 201 && response.body() != null) {
                            val registration = response.body() as Registration
                            registration.secret = request.secret
                            registration.unitId = request.unitId
                            addRegistration(registration)
                        } else {
                            Toast.makeText(activity, getString(R.string.unitReader_toast_registration_failed), Toast.LENGTH_LONG).show()
                        }

                    }
                })


            }
        }
    }

    private fun readRegistrationsFromFile(): MutableList<Registration> {
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

    private fun addRegistration(registration: Registration) {
        simpleAdapter.addItem(registration)
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = sharedPref.edit()
        editor.putLong("userId", registration.userId)
        editor.putString("apiKey", registration.apiKey)
        editor.apply()
        storeRegistrations(simpleAdapter.getList())
    }

    private fun storeRegistrations(registrations: MutableList<Registration>) {
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

    private fun removeRegistration(position: Int) {
        val registrationDeleted = simpleAdapter[position]

        val unregistrationRequest = UnregistrationRequest(registrationDeleted.unitId, registrationDeleted.apiKey, registrationDeleted.userId)
        SERVICE.unregisterUnit(unregistrationRequest).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.d(javaClass.name, "Service call failed")
                Toast.makeText(activity, getString(R.string.unitReader_toast_serviceCall_failed), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                if (response.code() == 200 || response.code() == 204) {
                    simpleAdapter.removeAt(position)
                    storeRegistrations(simpleAdapter.getList())
                } else {
                    Toast.makeText(activity, getString(R.string.unitReader_toast_unregistration_failed), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}// Required empty public constructor
