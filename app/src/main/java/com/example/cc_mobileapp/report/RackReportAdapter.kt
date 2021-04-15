package com.example.cc_mobileapp.report

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Rack
import kotlinx.android.synthetic.main.product_report_display_item.view.*
import kotlinx.android.synthetic.main.rack_report_display_item.view.*

class RackReportAdapter: RecyclerView.Adapter<RackReportAdapter.RackReportViewModel>()  {
    private var rack_report = mutableListOf<Rack>()

    var listener: RackReportRecycleViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = RackReportViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.rack_report_display_item, parent, false)
    )

    override fun getItemCount() = rack_report.size

    override fun onBindViewHolder(holder: RackReportViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder 2" )
        if(itemCount == 0)
        {
            holder.view.txtRackName.text = null
            holder.view.txtCurrentQty.text = null
        }
        else
        {
            holder.view.txtRackName.text = rack_report[position].rackName
            holder.view.txtCurrentQty.text = rack_report[position].currentQty.toString()
        }

    }

    fun setRackReportDetails(rackReport: List<Rack>?){
        Log.d("Check", "rack report set 2: $rackReport")
        this.rack_report = rackReport as MutableList<Rack>
        notifyDataSetChanged()
        Log.d("Check", "rack report set size: $itemCount")
    }

    class RackReportViewModel(val view: View) : RecyclerView.ViewHolder(view)

}