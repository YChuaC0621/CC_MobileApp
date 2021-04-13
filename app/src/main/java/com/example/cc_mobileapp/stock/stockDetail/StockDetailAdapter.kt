package com.example.cc_mobileapp.stock.stockDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
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
        holder.view.stockDetail_prodBarcode.text = stocksDetail[position].stockDetailProdBarcode.toString()
        holder.view.stockDetail_rackId.text = stocksDetail[position].stockDetailRackId
        holder.view.stockDetail_qty.text = stocksDetail[position].stockDetailQty.toString()
        holder.view.btn_edit_stockDetail.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksDetail[position])}
        holder.view.btn_delete_stockDetail.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksDetail[position])}
    // TODO require to calculate total price [Query]


    }

    fun setStocksDetail(stocksDetail: List<StockDetail>){
        this.stocksDetail = stocksDetail as MutableList<StockDetail>
        notifyDataSetChanged()
    }

    fun addStockDetail(stockDetail: StockDetail) {
        if (!stocksDetail.contains(stockDetail)) {
            stocksDetail.add(stockDetail)
        }else{
            val index = stocksDetail.indexOf(stockDetail)
            if(stockDetail.isDeleted){
                stocksDetail.removeAt(index)
            }else{ // for update product
                stocksDetail[index] = stockDetail
            }
        }
        notifyDataSetChanged()
    }

    class StockViewModel(val view: View) : RecyclerView.ViewHolder(view)
}