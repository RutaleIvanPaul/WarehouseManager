package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.R

class SalespersonBottomSheetRVAdapter(private val salespeople: Array<String>)
    :RecyclerView.Adapter<SalespersonBottomSheetRVAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView

        init {
            textView = itemView.findViewById(R.id.salespersonname)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.salesperson_recyclerview_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = salespeople[position]
    }

    override fun getItemCount() = salespeople.size


}