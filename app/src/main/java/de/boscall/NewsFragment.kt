package de.boscall


import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.boscall.dto.Date
import kotlinx.android.synthetic.main.fragment_news.*


/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    private val dateAdapter = DateAdapter(mutableListOf())

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_news, container, false)
    }

    fun initialize() {
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
