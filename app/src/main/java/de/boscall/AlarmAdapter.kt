package de.boscall

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.boscall.dto.Alarm
import kotlinx.android.synthetic.main.list_node.view.*

class AlarmAdapter(private val items: MutableList<Alarm>) : RecyclerView.Adapter<AlarmAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(alarm: Alarm) {
        items.add(alarm)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    operator fun get(position: Int): Alarm = items[position]

    fun getList(): MutableList<Alarm> = items

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_node, parent, false)) {

        fun bind(alarm: Alarm) = with(itemView) {
            tvTitle.text = alarm.title
            tvContent.text = alarm.text
        }
    }
}