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

    private var stocksIn = mutableListOf<StockIn>()
    var listener: StockInRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockInViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.stockin_display_item, parent, false)
    )

    override fun getItemCount() = stocksIn.size

    override fun onBindViewHolder(holder: StockInViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_stockInDate.text = stocksIn[position].stockInDate
        holder.view.txtView_stockInTime.text = stocksIn[position].stockInTime
        holder.view.btn_stockInSupplierId.text = stocksIn[position].stockInSupplierId
        holder.view.txtView_txtTotalPriceStockIn.text = stocksIn[position].totalProdPrice.toString()
        holder.view.btn_stockInSupplierId.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksIn[position])}
    }

    fun setStocksIn(stocksIn: List<StockIn>){
        Log.d("Check", "stock in set: $stocksIn")
        this.stocksIn = stocksIn as MutableList<StockIn>
        notifyDataSetChanged()
    }

    fun addStockIn(stockIn: StockIn) {
            Log.d("Check", "real time add stock in $stockIn")
            if (!stocksIn.contains(stockIn)) {
                stocksIn.add(stockIn)
            }else{  // only can perform add
        }
        notifyDataSetChanged()
    }

    class StockInViewModel(val view: View) : RecyclerView.ViewHolder(view)
}