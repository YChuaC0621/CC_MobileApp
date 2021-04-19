package com.example.cc_mobileapp.stock.stockOutDetail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.*
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.rack.RackViewModel
import com.example.cc_mobileapp.stock.stockDetail.AddStockOutDetailFragment
import com.example.cc_mobileapp.stock.stockDetail.StockViewModel
import com.example.cc_mobileapp.stock.stockOut.StockOutViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stockout_detail.*
import java.text.SimpleDateFormat
import java.util.*

class StockOutDetailFragment : Fragment(), StockOutDetailRecyclerViewClickListener {

    // variable declaration
    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbStockOutDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUTDETAIL)
    private val dbTempOut = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP_OUT)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockOutDetailAdapter()
    private lateinit var stockOutDetailViewModel: StockOutDetailViewModel
    private val sharedStockOutViewModel: StockOutViewModel by activityViewModels()
    private lateinit var rackViewModel: RackViewModel
    private lateinit var stockViewModel: StockViewModel
    private lateinit var productSnapshot: DataSnapshot
    private var totalPrice: Double = 0.0
    var productfromDB: Product? = null
    var callBackCount: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // access the information from view model
        stockViewModel = ViewModelProvider(this@StockOutDetailFragment).get(StockViewModel::class.java)
        productViewModel = ViewModelProvider(this@StockOutDetailFragment).get(ProductViewModel::class.java)
        stockOutDetailViewModel = ViewModelProvider(this@StockOutDetailFragment).get(StockOutDetailViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockOutDetailFragment).get(RackViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stockout_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // adapter and adapter listener set up on recycler view
        adapter.listener = this
        recycler_view_stockOutDetail.adapter = adapter

        // get data from database
        productViewModel.fetchProduct()
        stockOutDetailViewModel.fetchStockOutDetail()

        // get real time updates
        stockOutDetailViewModel.getRealtimeUpdates()

        // observe the changes in stocks out detail, if have changes, set the stocks out information and make changes on UI
        stockOutDetailViewModel.stocksOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksOutDetail(it)
            btn_stockOutDetailAdd.isEnabled = adapter.itemCount > 0
        })

        // observe the changes in stock out detail, if have changes, set the stocks out information and make changes on UI
        stockOutDetailViewModel.stockOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockOutDetail(it)
            btn_stockOutDetailSave.isEnabled = adapter.itemCount > 0
        })

        // button "+" is clicked, go to add fragment
        btn_stockOutDetailAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddStockOutDetailFragment())
            transaction.addToBackStack("addStockOutDetailFragment")
            transaction.commit()
        }

        // when button "save" is click save the stock out details
        btn_stockOutDetailSave.setOnClickListener {
            getProdsDetail()
        }
    }

    // get the current product details for update
    fun getProdsDetail() {
        readData(object : FirebaseCallback {
            override fun onCallBack(snapshot: DataSnapshot) {
                productSnapshot = snapshot
                Log.d("inside the on call back", snapshot.toString())
                var count: Int = 0
                var stockOutDetails = mutableListOf(stockOutDetailViewModel.stocksOutDetail)
                // iterate through each product for update
                stockOutDetails.listIterator().forEach {
                    it.value?.forEach {
                        dbStockOutDetail.push().setValue(it)
                        count += 1
                        stockUpdateProduct(it.stockOutDetailProdBarcode, it.stockOutDetailQty)
                        // calculate total price for each transaction
                        if (!(productfromDB?.prodPrice == null || it.stockOutDetailQty == null)) {
                            var price: Double = productfromDB?.prodPrice!!
                            var qty: Int? = it.stockOutDetailQty
                            var subtotal: Double = price.times(qty!!)
                            totalPrice = totalPrice.plus(subtotal)
                            stockUpdateQty()
                        }
                    }
                }
                // remove all the temporary stock details (for adding purpose)
                dbTempOut.removeValue()
                updateStockOutDetail()
            }
        })
    }

    // update the quantity for each of the product and the stock details
    private fun stockUpdateQty() {
    var stockOutDetails = mutableListOf(stockOutDetailViewModel.stocksOutDetail)
    stockOutDetails.listIterator().forEach {
        it.value?.forEach {
            var stockOutDetail = it
            var stockOutQty = it.stockOutDetailQty
            var stockInQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_STOCKDETAIL).orderByChild("stockDetailProdBarcode").equalTo(stockOutDetail.stockOutDetailProdBarcode.toString())
            stockInQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                var stockCallBack = 0
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (stockCallBack == 0) {
                        if (snapshot.exists()) {
                            for (stockInInfo in snapshot.children) {
                                var stockInDetail = stockInInfo.getValue(StockDetail::class.java)
                                stockInDetail?.stockDetailId = stockInInfo.key
                                if (stockOutQty!! != 0) {
                                    // stock in detail has more quantity than stock out detail
                                    if (stockOutQty!! <= stockInDetail?.stockDetailQty!!) {
                                        stockInDetail.stockDetailQty = stockInDetail.stockDetailQty!! - stockOutQty!!
                                        stockOutQty = 0
                                    } else {
                                        // stock in detail has more quantity than stock out detail
                                        stockOutQty = stockOutQty!! - stockInDetail.stockDetailQty!!
                                        stockInDetail.stockDetailQty = 0
                                    }
                                    // if stock in detail quantity =0, delete the stock in detail, else update the information
                                    if (stockInDetail.stockDetailQty == 0) {
                                        stockViewModel.deleteStockDetailinDB(stockInDetail)
                                    } else {
                                        stockViewModel.updateStockDetailinDB(stockInDetail)
                                    }
                                    stockCallBack += 1
                                }
                            }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}
    // update new stock out detail transaction information and store to database
    private fun updateStockOutDetail() {
        val stockOut = StockOut()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        var today = Calendar.getInstance().getTime()
        var malaysiaTime = Calendar.getInstance()
        malaysiaTime.add(Calendar.HOUR, 8)
        var todayTime = malaysiaTime.time
        stockOut.stockOutDate = dateFormat.format(today)
        stockOut.stockOutTime = timeFormat.format(todayTime)
        stockOut.stockOutClientId = sharedStockOutViewModel.stockOutClientId.value
        stockOut.totalProdPrice = totalPrice
        sharedStockOutViewModel.addStockOut(stockOut)
        viewModelStore.clear()
        requireActivity().supportFragmentManager.popBackStack(getCallerFragment(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // update for each of the product which carry out stock out
    private fun stockUpdateProduct(stockOutDetailBarcode: String?, stockOutDetailQty: Int?){
        productSnapshot.children.forEach {
            var tempProd: Product? = null
            tempProd = it.getValue(Product::class.java)
            var prodDBBarcode: String = tempProd?.prodBarcode.toString()
            if(prodDBBarcode.equals(stockOutDetailBarcode)){
                productfromDB = it.getValue(Product::class.java)
                productfromDB?.prodQty = productfromDB?.prodQty?.minus(stockOutDetailQty!!)
                dbProduct.child(it.key!!).setValue(productfromDB)
                        .addOnCompleteListener { it ->
                             if (it.isSuccessful) {
                                Log.d("check", "update successfully")
                            } else {
                                Log.d("check", "update successfully")
                            }
                        }
            }
        }
    }

    // get all product information from database
    private fun readData(firebaseCallback: FirebaseCallback){
        dbProduct.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (callBackCount == 0) {
                    firebaseCallback.onCallBack(snapshot)
                    callBackCount += 1
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // firebase callback interface
    private interface FirebaseCallback{
        fun onCallBack(snapshot: DataSnapshot)
    }

    // get the previous fragment name
    fun getCallerFragment(): String? {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
        Toast.makeText(requireContext(), fm.getBackStackEntryAt(count - 2).name, Toast.LENGTH_SHORT).show()
        return fm.getBackStackEntryAt(count - 2).name
    }

    // when the "edit" button on the recycler view is click, go to edit stock out detail fragment with passing parameter of stock out detail
    override fun onRecyclerViewItemClicked(view: View, stockOutDetail: StockOutDetail) {
        when(view.id){
            R.id.btn_edit_stockOutDetail -> {
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, EditStockOutDetailFragment(stockOutDetail))
                transaction.addToBackStack("editStockOutDetailFragment")
                transaction.commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbTempOut.removeValue()
    }
}

