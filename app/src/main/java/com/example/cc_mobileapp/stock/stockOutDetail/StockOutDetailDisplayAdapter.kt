package com.example.cc_mobileapp.stock.stockOutDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.model.StockOutDetail
import com.example.cc_mobileapp.stock.stockDetail.StockDetailRecyclerViewClickListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.stockdetail_display_item.view.*
import kotlinx.android.synthetic.main.stockdetaildisplay_display_item.view.*
import kotlinx.android.synthetic.main.stockoutdetaildisplay_display_item.view.*
import kotlinx.coroutines.*


class StockOutDetailDisplayAdapter(): RecyclerView.Adapter<StockOutDetailDisplayAdapter.StockOutDetailViewModel>(){
    // variable declaration
    private var stocksOutDetailDisplay = mutableListOf<StockOutDetail>()
    var listener: StockOutDetailRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockOutDetailViewModel(
            // inflate the stockoutdetail_display_item layout
            LayoutInflater.from(parent.context)
            .inflate(R.layout.stockoutdetaildisplay_display_item, parent, false)
    )

    // get total stocks out detail count
    override fun getItemCount() = stocksOutDetailDisplay.size

    // bind the information to the user interface
    override fun onBindViewHolder(holder: StockOutDetailViewModel, position: Int) {
        holder.view.stockOutDetailDisplay_prodBarcode.text = stocksOutDetailDisplay[position].stockOutDetailProdBarcode.toString()
        holder.view.stockOutDetailDisplay_qty.text = stocksOutDetailDisplay[position].stockOutDetailQty.toString()
        val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
        var price:Double? = 0.0
        GlobalScope.launch(Dispatchers.IO) {
            dbProduct.get().addOnSuccessListener {
                if (it.exists()) {
                    it.children.forEach {
                        var prod: Product? = it.getValue(Product::class.java)
                        if (prod?.prodBarcode == stocksOutDetailDisplay[position].stockOutDetailProdBarcode) {
                            var price: Double? = prod?.prodPrice!! * stocksOutDetailDisplay[position].stockOutDetailQty!!
                            holder.view.stockOutDetailDisplay_totalPrice.text = String.format("%.2f",price)
                        }
                    }
                }
            }
        }
    }

    // set the stocks out detail information
    fun setStocksOutDetail(stocksOutDetail: List<StockOutDetail>){
        this.stocksOutDetailDisplay = stocksOutDetail as MutableList<StockOutDetail>
        //get real time updates
        notifyDataSetChanged()
    }

    // add stock out detail
    fun addStockOutDetail(stockOutDetail: StockOutDetail) {
        if (!stocksOutDetailDisplay.contains(stockOutDetail)) {
            stocksOutDetailDisplay.add(stockOutDetail)
        }else{
        }
        //get real time updates
        notifyDataSetChanged()
    }


    class StockOutDetailViewModel(val view: View) : RecyclerView.ViewHolder(view)
}
