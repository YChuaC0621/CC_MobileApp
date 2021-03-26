package com.example.cc_mobileapp.stock.stockDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockDetail
import kotlinx.android.synthetic.main.stockdetail_display_item.view.*


class StockDetailAdapter : RecyclerView.Adapter<StockDetailAdapter.StockViewModel>(){

    private var stocksDetail = mutableListOf<StockDetail>()
    var listener: StockDetailRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.stockdetail_display_item, parent, false)
    )

    override fun getItemCount() = stocksDetail.size

    override fun onBindViewHolder(holder: StockViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.stockDetail_prodBarcode.text = stocksDetail[position].stockDetailProdBarcode
        holder.view.stockDetail_rackId.text = stocksDetail[position].stockDetailRackId
        holder.view.stockDetail_rowNum.text = stocksDetail[position].stockDetailRowNum
        holder.view.stockDetail_qty.text = stocksDetail[position].stockDetailQty
        // TODO require to calculate total price [Query]
        holder.view.stockDetail_totalPrice.text = "20.00"
        holder.view.btn_edit_stockDetail.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksDetail[position])}
        holder.view.btn_delete_stockDetail.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksDetail[position])}
    }

    fun setStocksDetail(stocksDetail: List<StockDetail>){
        Log.d("Check", "stock in set: $stocksDetail")
        this.stocksDetail = stocksDetail as MutableList<StockDetail>
        notifyDataSetChanged()
    }

    fun addStockDetail(stockDetail: StockDetail) {
        Log.d("Check", "real time add stock in $stockDetail")
        if (!stocksDetail.contains(stockDetail)) {
            stocksDetail.add(stockDetail)
        }else{  // only can perform add
        }
        notifyDataSetChanged()
    }

//    fun clearAdapterValue(){
//        stocksDetail.
//    }

    class StockViewModel(val view: View) : RecyclerView.ViewHolder(view)
}