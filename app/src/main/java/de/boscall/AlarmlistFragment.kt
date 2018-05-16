package de.boscall


import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */
class AlarmlistFragment : Fragment() {

    private var listNotes = ArrayList<ListNode>()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_alarmlist, container, false)

        fillAlarmList(view)

        return view
    }

    fun fillAlarmList(view: View?) {
        listNotes.add(ListNode(1, "JavaSampleApproach", "Java technology, Spring Framework - approach to Java by Sample."))
        listNotes.add(ListNode(2, "Kotlin Android Tutorial", "Create tutorial for people to learn Kotlin Android. Kotlin is now an official language on Android. It's expressive, concise, and powerful. Best of all, it's interoperable with our existing Android languages and runtime."))
        listNotes.add(ListNode(3, "Android Studio", "Android Studio 3.0 provides helpful tools to help you start using Kotlin. Convert entire Java files or convert code snippets on the fly when you paste Java code into a Kotlin file."))
        listNotes.add(ListNode(4, "Java Android Tutorial", "Create tutorial for people to learn Java Android. Learn Java in a greatly improved learning environment with more lessons, real practice opportunity, and community support."))
        listNotes.add(ListNode(5, "Spring Boot Tutorial", "Spring Boot help build stand-alone, production Spring Applications easily, less configuration then rapidly start new projects."))
        listNotes.add(ListNode(6, "JavaSampleApproach", "Java technology, Spring Framework - approach to Java by Sample."))
        listNotes.add(ListNode(7, "Kotlin Android Tutorial", "Create tutorial for people to learn Kotlin Android. Kotlin is now an official language on Android. It's expressive, concise, and powerful. Best of all, it's interoperable with our existing Android languages and runtime."))
        listNotes.add(ListNode(8, "Android Studio", "Android Studio 3.0 provides helpful tools to help you start using Kotlin. Convert entire Java files or convert code snippets on the fly when you paste Java code into a Kotlin file."))
        listNotes.add(ListNode(9, "Java Android Tutorial", "Create tutorial for people to learn Java Android. Learn Java in a greatly improved learning environment with more lessons, real practice opportunity, and community support."))
        listNotes.add(ListNode(10, "Spring Boot Tutorial", "Spring Boot help build stand-alone, production Spring Applications easily, less configuration then rapidly start new projects."))

        var notesAdapter = NotesAdapter(listNotes)
        var liste = view?.findViewById<ListView>(R.id.alarmList) as ListView
        liste.adapter = notesAdapter
    }

    inner class NotesAdapter(private var notesList: ArrayList<ListNode>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                val inflater: LayoutInflater = LayoutInflater.from(context)
                view = inflater.inflate(R.layout.list_node, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.tvTitle.text = notesList[position].title
            vh.tvContent.text = notesList[position].content

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView

        init {
            this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
            this.tvContent = view.findViewById<TextView>(R.id.tvContent) as TextView
        }
    }

}
