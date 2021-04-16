package com.example.cc_mobileapp.stock.stockOutDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockOutDetail
import com.example.cc_mobileapp.stock.stockDetail.StockDetailRecyclerViewClickListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.stockdetail_display_item.view.*
import kotlinx.android.synthetic.main.stockoutdetail_display_item.view.*
import kotlinx.coroutines.*
import androidx.fragment.app.Fragment


class StockOutDetailAdapter(): RecyclerView.Adapter<StockOutDetailAdapter.StockOutDetailViewModel>(){
    private var stocksOutDetail = mutableListOf<StockOutDetail>()
    var listener: StockOutDetailRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = StockOutDetailViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.stockoutdetail_display_item, parent, false)
    )

    override fun getItemCount() = stocksOutDetail.size

    private lateinit var productSnapshot: DataSnapshot

    fun setStocksOutDetail(stocksOutDetail: List<StockOutDetail>){
        this.stocksOutDetail = stocksOutDetail as MutableList<StockOutDetail>
        notifyDataSetChanged()
    }

    fun addStockOutDetail(stockOutDetail: StockOutDetail) {
        if (!stocksOutDetail.contains(stockOutDetail)) {
            stocksOutDetail.add(stockOutDetail)
        }else{
            val index = stocksOutDetail.indexOf(stockOutDetail)
            if(stockOutDetail.isDeleted){
                stocksOutDetail.removeAt(index)
            }else{ // for update product
                stocksOutDetail[index] = stockOutDetail
            }
        }
        notifyDataSetChanged()
    }

    class StockOutDetailViewModel(val view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: StockOutDetailViewModel, position: Int) {
        holder.view.stockOutDetail_prodBarcode.text = stocksOutDetail[position].stockOutDetailProdBarcode.toString()
        holder.view.stockOutDetail_qty.text = stocksOutDetail[position].stockOutDetailQty.toString()
        holder.view.btn_edit_stockOutDetail.setOnClickListener { listener?.onRecyclerViewItemClicked(it, stocksOutDetail[position])}
        val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
        var price:Double? = 0.0
        GlobalScope.launch(Dispatchers.IO) {
            dbProduct.get().addOnSuccessListener {
                if (it.exists()) {
                    it.children.forEach {
                        var prod: Product? = it.getValue(Product::class.java)
                        if (prod?.prodBarcode == stocksOutDetail[position].stockOutDetailProdBarcode) {
                            var price: Double? = prod?.prodPrice!! * stocksOutDetail[position].stockOutDetailQty!!
                            holder.view.stockOutDetail_totalPrice.text = price.toString()
                        }
                    }

                }
            }
        }
    }
}
