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
import com.example.cc_mobileapp.stock.stockIn.StockInRecyclerViewClickListener
import kotlinx.android.synthetic.main.client_display_item.view.*
import kotlinx.android.synthetic.main.stockin_display_item.view.*
import kotlinx.android.synthetic.main.stockout_display_item.view.*

class StockOutAdapter : RecyclerView.Adapter<StockOutAdapter.StockOutViewModel>(){
    // variable declaration
    private var stocksOut = mutableListOf<StockOut>()
    var listener: StockOutRecyclerViewClickListener? = null


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = StockOutViewModel(
            // inflate the stockout_display_item layout
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.stockout_display_item, parent, false)
    )
    // get total stocks out count
    override fun getItemCount() = stocksOut.size
    // bind the information to the user interface
    override fun onBindViewHolder(holder: StockOutViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_stockOutDate.text = stocksOut[position].stockOutDate
        holder.view.txtView_stockOutTime.text = stocksOut[position].stockOutTime
        holder.view.btn_stockOutClientId.text = stocksOut[position].stockOutClientId
        holder.view.txtView_txtTotalPriceStockOut.text = stocksOut[position].totalProdPrice.toString().trim()
        holder.view.btn_stockOutClientId.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksOut[position])}

    }

    // set the stocks out information
    fun setStocksOut(stocksOut: List<StockOut>){
        Log.d("Check", "stock Out set: $stocksOut")
        this.stocksOut = stocksOut as MutableList<StockOut>
        // real time changes
        notifyDataSetChanged()
    }

    // add stocks out information
    fun addStockOut(stockOut: StockOut) {
        Log.d("Check", "real time add stock Out $stockOut")
        if (!stocksOut.contains(stockOut)) {
            stocksOut.add(stockOut)
        }else{  // only can perform add
        }
        // real time changes
        notifyDataSetChanged()
    }

    class StockOutViewModel(val view: View) : RecyclerView.ViewHolder(view)
}