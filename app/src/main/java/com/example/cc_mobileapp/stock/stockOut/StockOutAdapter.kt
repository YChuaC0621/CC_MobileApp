package com.example.cc_mobileapp.stock.stockOut

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.ClientRecyclerViewClickListener
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.model.StockOut
import com.example.cc_mobileapp.stock.stockIn.StockInAdapter
import kotlinx.android.synthetic.main.client_display_item.view.*
import kotlinx.android.synthetic.main.stockin_display_item.view.*
import kotlinx.android.synthetic.main.stockout_display_item.view.*

class StockOutAdapter : RecyclerView.Adapter<StockOutAdapter.StockOutViewModel>(){

    private var stocksOut = mutableListOf<StockOut>()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = StockOutViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.stockout_display_item, parent, false)
    )

    override fun getItemCount() = stocksOut.size

    override fun onBindViewHolder(holder: StockOutViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_stockOutDateTime.text = stocksOut[position].stockOutDateTime
        holder.view.btn_stockOutClientId.text = stocksOut[position].stockOutClientId
    }

    fun setStocksOut(stocksOut: List<StockOut>){
        Log.d("Check", "stock Out set: $stocksOut")
        this.stocksOut = stocksOut as MutableList<StockOut>
        notifyDataSetChanged()
    }

    fun addStockOut(stockOut: StockOut) {
        Log.d("Check", "real time add stock Out $stockOut")
        if (!stocksOut.contains(stockOut)) {
            stocksOut.add(stockOut)
        }else{  // only can perform add
        }
        notifyDataSetChanged()
    }

    class StockOutViewModel(val view: View) : RecyclerView.ViewHolder(view)
}