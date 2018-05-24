package de.boscall


import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject
import de.boscall.constants.ServiceConfiguration
import de.boscall.dbTasks.UpdateDatesTask
import de.boscall.dto.Date
import de.boscall.services.BosCallWebAPIService
import kotlinx.android.synthetic.main.fragment_news.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    private val SERVICE = Retrofit.Builder().baseUrl(ServiceConfiguration.API_ADDRESS).addConverterFactory(GsonConverterFactory.create()).build().create(BosCallWebAPIService::class.java)
    private val dateAdapter = DateAdapter(mutableListOf())

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_news, container, false)
    }

    fun initialize() {
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val userId = sharedPref.getLong("userId", -1)
        val apiKey = sharedPref.getString("apiKey", null)

        if (userId > -1 && apiKey != null) {
            val result: Call<List<JsonObject>> = SERVICE.updateDates(userId, apiKey)
            UpdateDatesTask(activity).execute(result)
        }

        val viewModel = ViewModelProviders.of(activity as FragmentActivity).get(DatesViewModel::class.java)
        viewModel.getDates().observe(activity as FragmentActivity, object : Observer<MutableList<Date>> {
            override fun onChanged(t: MutableList<Date>?) {
                dateAdapter.setDates(t!!)
            }
        })
        //AddDatessToAdapterTask(dateAdapter, activity).execute()

        terminList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        terminList.layoutManager = LinearLayoutManager(activity)
        terminList.adapter = dateAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }

}// Required empty public constructor
