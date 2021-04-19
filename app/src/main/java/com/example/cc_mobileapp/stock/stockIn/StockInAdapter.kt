package com.example.cc_mobileapp.stock.stockIn

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.ClientRecyclerViewClickListener
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.stock.stockDetail.StockDetailRecyclerViewClickListener
import kotlinx.android.synthetic.main.client_display_item.view.*
import kotlinx.android.synthetic.main.stockdetail_display_item.view.*
import kotlinx.android.synthetic.main.stockin_display_item.view.*

class StockInAdapter : RecyclerView.Adapter<StockInAdapter.StockInViewModel>(){
    // variable declaration
    private var stocksIn = mutableListOf<StockIn>()
    var listener: StockInRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockInViewModel(
            // inflate the stockin_display_item layout
            LayoutInflater.from(parent.context)
            .inflate(R.layout.stockin_display_item, parent, false)
    )

    // get total stocks in count
    override fun getItemCount() = stocksIn.size

    // bind the information to the user interface
    override fun onBindViewHolder(holder: StockInViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_stockInDate.text = stocksIn[position].stockInDate
        holder.view.txtView_stockInTime.text = stocksIn[position].stockInTime
        holder.view.btn_stockInSupplierId.text = stocksIn[position].stockInSupplierId
        var price = stocksIn[position].totalProdPrice
        holder.view.txtView_txtTotalPriceStockIn.text = String.format("%.2f",price)
        holder.view.btn_stockInSupplierId.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksIn[position])}
    }

    // set the stocks in information
    fun setStocksIn(stocksIn: List<StockIn>){
        Log.d("Check", "stock in set: $stocksIn")
        this.stocksIn = stocksIn as MutableList<StockIn>
        // real time changes
        notifyDataSetChanged()
    }

    // add stocks in information
    fun addStockIn(stockIn: StockIn) {
            Log.d("Check", "real time add stock in $stockIn")
            if (!stocksIn.contains(stockIn)) {
                stocksIn.add(stockIn)
            }else{  // only can perform add
        }
        // real time changes
        notifyDataSetChanged()
    }

    class StockInViewModel(val view: View) : RecyclerView.ViewHolder(view)
}