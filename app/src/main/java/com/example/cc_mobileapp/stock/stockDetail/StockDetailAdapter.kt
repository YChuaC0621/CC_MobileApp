package com.example.cc_mobileapp.stock.stockDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.stockdetail_display_item.view.*
import kotlinx.coroutines.*


class StockDetailAdapter(): RecyclerView.Adapter<StockDetailAdapter.StockViewModel>(){
    // variable declaration
    private var stocksDetail = mutableListOf<StockDetail>()
    var listener: StockDetailRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockViewModel(
            // inflate the stockdetail_display_item layout
            LayoutInflater.from(parent.context)
            .inflate(R.layout.stockdetail_display_item, parent, false)
    )

    // get total stocks detail count
    override fun getItemCount() = stocksDetail.size

    // bind the information to the user interface
    override fun onBindViewHolder(holder: StockViewModel, position: Int) {
        if(position < stocksDetail.size && stocksDetail.size!= position){
            holder.view.stockDetail_prodBarcode.text = stocksDetail[position].stockDetailProdBarcode.toString()
            holder.view.stockDetail_rackId.text = stocksDetail[position].stockDetailRackId
            holder.view.stockDetail_qty.text = stocksDetail[position].stockDetailQty.toString()
            holder.view.btn_edit_stockDetail.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksDetail[position])}
            val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
            GlobalScope.launch(Dispatchers.IO) {
                dbProduct.get().addOnSuccessListener {
                    if (it.exists()) {
                        it.children.forEach {
                            if(position < stocksDetail.size && stocksDetail.size!= position) {
                                var prod: Product? = it.getValue(Product::class.java)
                                if (prod?.prodBarcode == stocksDetail[position].stockDetailProdBarcode) {
                                    var price: Double? = prod?.prodPrice!! * stocksDetail[position].stockDetailQty!!
                                    holder.view.stockDetail_totalPrice.text = price.toString().trim()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // set the stocks detail information
    fun setStocksDetail(stocksDetail: List<StockDetail>){
        this.stocksDetail = stocksDetail as MutableList<StockDetail>
        notifyDataSetChanged()
    }

    // add stocks detail information
    fun addStockDetail(stockDetail: StockDetail) {
        if (!stocksDetail.contains(stockDetail)) {
            stocksDetail.add(stockDetail)
        }else{
            val index = stocksDetail.indexOf(stockDetail)
            if(stockDetail.isDeleted){ // delete stocks detail
                stocksDetail.remove(stockDetail)
            }else{ // for update stocks detail
                stocksDetail[index] = stockDetail
            }
        }
        // real time changes
        notifyDataSetChanged()
    }

    class StockViewModel(val view: View) : RecyclerView.ViewHolder(view)


}
