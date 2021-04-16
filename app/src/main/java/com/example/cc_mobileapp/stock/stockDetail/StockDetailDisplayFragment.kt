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

    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbPermanentStock = FirebaseDatabase.getInstance().getReference(Constant.NODE_PERM_STOCKINDETAIL)
    private val dbTemp = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockDetailDisplayAdapter()
    private lateinit var stockViewModel: StockViewModel
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private lateinit var rackViewModel: RackViewModel
    var stockDetailID: String = stockIn.stockInId.toString()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        productViewModel = ViewModelProvider(this@StockDetailDisplayFragment).get(ProductViewModel::class.java)
        stockViewModel = ViewModelProvider(this@StockDetailDisplayFragment).get(StockViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockDetailDisplayFragment).get(RackViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stock_detail_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view_stockDetail_display.adapter = adapter

        stockViewModel.fetchStockDetailDisplay(stockDetailID)

        stockViewModel.getRealtimeUpdates()

        stockViewModel.stocksDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksDetail(it)
        })

        stockViewModel.stockDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockDetail(it)
        })
    }

}

