package com.example.cc_mobileapp.stock.stockDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.rack.RackViewModel
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.recycler_view_stockDetail
import kotlinx.android.synthetic.main.fragment_stock_detail_display.*
import java.text.SimpleDateFormat
import java.util.*

class StockDetailDisplayFragment(stockIn:StockIn) : Fragment(){

    // variable declaration
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockDetailDisplayAdapter()
    private lateinit var stockViewModel: StockViewModel
    private lateinit var rackViewModel: RackViewModel
    var stockDetailID: String = stockIn.stockInId.toString()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // access the information from view model
        productViewModel = ViewModelProvider(this@StockDetailDisplayFragment).get(ProductViewModel::class.java)
        stockViewModel = ViewModelProvider(this@StockDetailDisplayFragment).get(StockViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockDetailDisplayFragment).get(RackViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_detail_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // adapter set up on recycler view
        recycler_view_stockDetail_display.adapter = adapter

        // get data from database
        stockViewModel.fetchStockDetailDisplay(stockDetailID)

        // get real time updates
        stockViewModel.getRealtimeUpdates()

        // observe the changes in stocks detail, if have changes, set the products information and make changes on UI
        stockViewModel.stocksDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksDetail(it)
        })
        // observe the changes in stock detail, if have changes, set the products information and make changes on UI
        stockViewModel.stockDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockDetail(it)
        })
    }

}

