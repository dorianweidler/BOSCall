package de.boscall

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.boscall.dto.Registration
import kotlinx.android.synthetic.main.unit_item.view.*

class SimpleRegistrationAdapter(private val items: MutableList<Registration>) : RecyclerView.Adapter<SimpleRegistrationAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(registration: Registration) {
        items.add(registration)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    operator fun get(position: Int): Registration = items[position]

    fun getList(): MutableList<Registration> = items

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false)) {

        fun bind(registration: Registration) = with(itemView) {
            unitName.text = registration.unitName
        }
    }
}