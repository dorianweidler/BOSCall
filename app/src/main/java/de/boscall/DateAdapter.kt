package de.boscall

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.boscall.dto.Date
import kotlinx.android.synthetic.main.date_node.view.*

class DateAdapter(private var items: MutableList<Date>) : RecyclerView.Adapter<DateAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(date: Date) {
        items.add(date)
        notifyItemInserted(items.size)
    }

    fun setDates(dates: MutableList<Date>) {
        this.items = dates
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    operator fun get(position: Int): Date = items[position]

    fun getList(): MutableList<Date> = items

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.date_node, parent, false)) {

        fun bind(date: Date) = with(itemView) {

            tvDateTitle.text = date.title
            tvDate.text = date.getFormatedDate()

            itemView.setOnClickListener {
                AlertDialog.Builder(context)
                        .setTitle(date.getFormatedDate() + ": " + date.title)
                        .setMessage(date.text)
                        .setIcon(R.drawable.ic_dialog_alert_black_24dp)
                        .setPositiveButton(R.string.dlg_OK_btnOk, { dialog, which ->
                            //Nothing to do
                        })
                        .setCancelable(false)
                        .show()
            }
        }
    }
}