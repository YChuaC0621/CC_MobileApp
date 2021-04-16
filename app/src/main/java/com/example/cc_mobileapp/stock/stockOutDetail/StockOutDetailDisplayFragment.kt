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

    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbPermanentStock = FirebaseDatabase.getInstance().getReference(Constant.NODE_PERM_STOCKINDETAIL)
    private val dbTemp = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockOutDetailDisplayAdapter()
    private lateinit var stockOutDetailViewModel: StockOutDetailViewModel
    private val sharedStockOutViewModel: StockOutViewModel by activityViewModels()
    private lateinit var rackViewModel: RackViewModel
    var stockOutID: String = stockOut.stockOutId.toString()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        productViewModel = ViewModelProvider(this@StockOutDetailDisplayFragment).get(ProductViewModel::class.java)
        stockOutDetailViewModel = ViewModelProvider(this@StockOutDetailDisplayFragment).get(StockOutDetailViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockOutDetailDisplayFragment).get(RackViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stockout_detail_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view_stockOutDetail_display.adapter = adapter

        stockOutDetailViewModel.fetchStockOutDetailDisplay(stockOutID)

        stockOutDetailViewModel.getRealtimeUpdates()

        stockOutDetailViewModel.stocksOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksOutDetail(it)
        })

        stockOutDetailViewModel.stockOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockOutDetail(it)
        })
    }

}

