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
import java.util.*

class StockOutDetailFragment : Fragment(), StockOutDetailRecyclerViewClickListener {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        stockViewModel = ViewModelProvider(this@StockOutDetailFragment).get(StockViewModel::class.java)
        productViewModel = ViewModelProvider(this@StockOutDetailFragment).get(ProductViewModel::class.java)
        stockOutDetailViewModel = ViewModelProvider(this@StockOutDetailFragment).get(StockOutDetailViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockOutDetailFragment).get(RackViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stockout_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_stockOutDetail.adapter = adapter

        productViewModel.fetchProduct()

        stockOutDetailViewModel.fetchStockOutDetail()

        stockOutDetailViewModel.getRealtimeUpdates()

        stockOutDetailViewModel.stocksOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksOutDetail(it)
            btn_stockOutDetailAdd.isEnabled = adapter.itemCount > 0
        })

        stockOutDetailViewModel.stockOutDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockOutDetail(it)
            btn_stockOutDetailSave.isEnabled = adapter.itemCount > 0
        })

        btn_stockOutDetailAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddStockOutDetailFragment())
            transaction.addToBackStack("addStockOutDetailFragment")
            transaction.commit()
        }


        btn_stockOutDetailSave.setOnClickListener {
            getProdsDetail()
        }
    }

    private lateinit var productSnapshot: DataSnapshot
    private lateinit var stockSnapshot: DataSnapshot
    private var totalPrice: Double = 0.0
    fun getProdsDetail() {
        readData(object : FirebaseCallback {
            override fun onCallBack(snapshot: DataSnapshot) {
                productSnapshot = snapshot
                Log.d("inside the on call back", snapshot.toString())
                var count: Int = 0
                var stockOutDetails = mutableListOf(stockOutDetailViewModel.stocksOutDetail)
                stockOutDetails.listIterator().forEach {
                    it.value?.forEach {
                        // TODO stock status update here
                        dbStockOutDetail.push().setValue(it)
                        count += 1
                        stockUpdateProduct(it.stockOutDetailProdBarcode, it.stockOutDetailQty)
                        if (!(productfromDB?.prodPrice == null || it.stockOutDetailQty == null)) {
                            var price: Double = productfromDB?.prodPrice!!
                            var qty: Int? = it.stockOutDetailQty
                            var subtotal: Double = price.times(qty!!)
                            totalPrice = totalPrice.plus(subtotal)
                            stockUpdateQty()
                        }
                    }
                }
                dbTempOut.removeValue()
                updateStockOutDetail()
            }
        })

    }



//    private fun stockUpdateStockInQty(stockOutDetailProdBarcode: String?, stockOutDetailQty: Int?) {
//        var stockOutDetailQty = stockOutDetailQty
//        GlobalScope.launch(Dispatchers.IO){
//            dbStockInDetail.orderByChild("stockDetailProdBarcode").equalTo(stockOutDetailProdBarcode).get().addOnSuccessListener {
//                if(it.exists()){
//                    it.children.forEach {
//                        if(stockOutDetailQty!! > 0) {
//                            var stockInUpdate = it.getValue(StockDetail::class.java)
//                            if (stockInUpdate?.stockDetailQty!! >= stockOutDetailQty!!) {
//                                stockInUpdate.stockDetailQty = stockInUpdate.stockDetailQty!! - stockOutDetailQty!!
//                                stockOutDetailQty = 0
//                            } else {
//                                stockOutDetailQty = stockOutDetailQty!! - stockInUpdate.stockDetailQty!!
//                                stockInUpdate.stockDetailQty = 0
//                            }
//                            stockInDetailViewModel.updateStockDetail(stockInUpdate)
//                        }
//                    }
//                }
//            }
//        }
//    }
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
                                if (stockOutQty!! > 0) {
                                    if (stockOutQty!! <= stockInDetail?.stockDetailQty!!) {
                                        stockInDetail.stockDetailQty = stockInDetail.stockDetailQty!! - stockOutQty!!
                                        stockOutQty = 0
                                        if (stockInDetail.stockDetailQty == 0) {
                                        }
                                    } else {
                                        stockOutQty = stockOutQty!! - stockInDetail.stockDetailQty!!
                                        stockInDetail.stockDetailQty = 0
                                    }
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
//    private fun stockUpdateQty() {
//        stockOutDetails.listIterator().forEach {
//            it.value?.forEach {
//                var stockOutDetail = it
//                var stockOutQty = stockOutDetail.stockOutDetailQty
//                stockSnapshot.children.forEach {
//                    if(stockOutQty ==0){
//                        var stockInDetail = it.getValue(StockDetail::class.java)
//                        if(stockOutDetail.stockOutDetailProdBarcode == stockInDetail?.stockDetailProdBarcode){
//                            if(stockInDetail.stockDetailQty > stockOutQty)
//                        }
//                    }
//                }
//            }
//        }
//    }

//        var stockOutDetailQty = stockOutDetailQty
//        stockSnapshot.children.forEach {
//            var stockDB: StockDetail? =null
//            stockDB = it.getValue(StockDetail::class.java)
//
//            if(stockDB?.stockDetailProdBarcode.equals(stockOutDetailProdBarcode)){
//                if(stockOutDetailQty!! > 0) {
//                    if (stockDB?.stockDetailQty!! >= stockOutDetailQty!!) {
//                        stockDB.stockDetailQty = stockDB.stockDetailQty!! - stockOutDetailQty!!
//                        stockOutDetailQty = 0
//                    } else {
//                        stockOutDetailQty = stockOutDetailQty!! - stockDB.stockDetailQty!!
//                        stockDB.stockDetailQty = 0
//                    }
//                    stockInDetailViewModel.updateStockDetail(stockDB)
//                }
//            }


    private fun updateStockOutDetail() {
        val stockOut = StockOut()
        var today = Calendar.getInstance()
        today.add(Calendar.HOUR, 8)
        stockOut.stockOutDateTime = today.time.toString()
        stockOut.stockOutClientId = sharedStockOutViewModel.stockOutClientId.value
        stockOut.totalProdPrice = totalPrice
        sharedStockOutViewModel.addStockOut(stockOut)
        viewModelStore.clear()
        requireActivity().supportFragmentManager.popBackStack(getCallerFragment(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


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

    var productfromDB: Product? = null
    var callBackCount: Int = 0

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

    private fun readStockData(firebaseCallback: FirebaseCallback){
        dbStockInDetail.addValueEventListener(object : ValueEventListener {
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

    private interface FirebaseCallback{
        fun onCallBack(snapshot: DataSnapshot)
    }

    fun getCallerFragment(): String? {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
        Toast.makeText(requireContext(), fm.getBackStackEntryAt(count - 2).name, Toast.LENGTH_SHORT).show()
        return fm.getBackStackEntryAt(count - 2).name
    }

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

