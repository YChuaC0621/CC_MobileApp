package com.example.cc_mobileapp.stock.stockOutDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockOut
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.rack.RackViewModel
import com.example.cc_mobileapp.stock.stockOut.StockOutViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail_display.*
import kotlinx.android.synthetic.main.fragment_stockout_detail_display.*
import java.util.*

class StockOutDetailDisplayFragment(stockOut: StockOut) : Fragment(){

    // variable declaration
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockOutDetailDisplayAdapter()
    private lateinit var stockOutDetailViewModel: StockOutDetailViewModel
    private lateinit var rackViewModel: RackViewModel
    var stockOutID: String = stockOut.stockOutId.toString()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // access the information from view model
        productViewModel = ViewModelProvider(this@StockOutDetailDisplayFragment).get(ProductViewModel::class.java)
        stockOutDetailViewModel = ViewModelProvider(this@StockOutDetailDisplayFragment).get(StockOutDetailViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockOutDetailDisplayFragment).get(RackViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stockout_detail_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // adapter set up on recycler view
        recycler_view_stockOutDetail_display.adapter = adapter

        // get data from database
        stockOutDetailViewModel.fetchStockOutDetailDisplay(stockOutID)

        // get real time updates
        stockOutDetailViewModel.getRealtimeUpdates()

        // observe the changes in stocks detail, if have changes, set the products information and make changes on UI
        stockOutDetailViewModel.stocksOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksOutDetail(it)
        })

        // observe the changes in stock detail, if have changes, set the products information and make changes on UI
        stockOutDetailViewModel.stockOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockOutDetail(it)
        })
    }

}

