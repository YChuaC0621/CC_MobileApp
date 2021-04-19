package com.example.cc_mobileapp.stock.stockDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.stockdetail_display_item.view.*
import kotlinx.android.synthetic.main.stockdetaildisplay_display_item.view.*
import kotlinx.coroutines.*


class StockDetailDisplayAdapter(): RecyclerView.Adapter<StockDetailDisplayAdapter.StockViewModel>(){
    // variable declaration
    private var stocksDetailDisplay = mutableListOf<StockDetail>()
    var listener: StockDetailRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockViewModel(
            // inflate the stockdetail_display_item layout
            LayoutInflater.from(parent.context)
            .inflate(R.layout.stockdetaildisplay_display_item, parent, false)
    )

    // get total stocks detail count
    override fun getItemCount() = stocksDetailDisplay.size

    // bind the information to the user interface
    override fun onBindViewHolder(holder: StockViewModel, position: Int) {
        holder.view.stockDetailDisplay_prodBarcode.text = stocksDetailDisplay[position].stockDetailProdBarcode.toString()
        holder.view.stockDetailDisplay_rackId.text = stocksDetailDisplay[position].stockDetailRackId
        holder.view.stockDetailDisplay_qty.text = stocksDetailDisplay[position].stockDetailQty.toString()
        val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
        var price:Double? = 0.0
        GlobalScope.launch(Dispatchers.IO) {
            dbProduct.get().addOnSuccessListener {
                if (it.exists()) {
                    it.children.forEach {
                        var prod: Product? = it.getValue(Product::class.java)
                        if (prod?.prodBarcode == stocksDetailDisplay[position].stockDetailProdBarcode) {
                            var price: Double? = prod?.prodPrice!! * stocksDetailDisplay[position].stockDetailQty!!
                            holder.view.stockDetailDisplay_totalPrice.text = price.toString()
                        }
                    }
                }
            }
        }
    }

    // set the stocks detail information
    fun setStocksDetail(stocksDetail: List<StockDetail>){
        this.stocksDetailDisplay = stocksDetail as MutableList<StockDetail>
        //get real time updates
        notifyDataSetChanged()
    }

    // add stock detail
    fun addStockDetail(stockDetail: StockDetail) {
        if (!stocksDetailDisplay.contains(stockDetail)) {
            stocksDetailDisplay.add(stockDetail)
        }else{
        }
        //get real time updates
        notifyDataSetChanged()
    }


    class StockViewModel(val view: View) : RecyclerView.ViewHolder(view)
}
