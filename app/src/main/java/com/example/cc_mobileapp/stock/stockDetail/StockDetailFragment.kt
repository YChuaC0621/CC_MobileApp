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
import com.example.cc_mobileapp.model.StockOut
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.rack.RackViewModel
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail_display.*
import java.text.SimpleDateFormat
import java.util.*

class StockDetailFragment : Fragment(), StockDetailRecyclerViewClickListener {

    // variable declaration
    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbPermanentStock = FirebaseDatabase.getInstance().getReference(Constant.NODE_PERM_STOCKINDETAIL)
    private val dbTemp = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockDetailAdapter()
    private lateinit var stockViewModel: StockViewModel
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private lateinit var rackViewModel: RackViewModel
    private lateinit var productSnapshot: DataSnapshot
    private var totalPrice: Double = 0.0
    var productfromDB: Product? = null
    var callBackCount: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // access the information from view model
        productViewModel = ViewModelProvider(this@StockDetailFragment).get(ProductViewModel::class.java)
        stockViewModel = ViewModelProvider(this@StockDetailFragment).get(StockViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockDetailFragment).get(RackViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // adapter and adapter listener set up on recycler view
        adapter.listener = this
        recycler_view_stockDetail.adapter = adapter

        // get data from database
        productViewModel.fetchProduct()
        stockViewModel.fetchStockDetail()

        // get real time updates
        stockViewModel.getRealtimeUpdates()

        // observe the changes in stocks detail, if have changes, set the stocks in information and make changes on UI
        stockViewModel.stocksDetail.observe(viewLifecycleOwner, Observer {
            adapter.setStocksDetail(it)
            btn_stockDetailSave.isEnabled = adapter.itemCount > 0
        })

        // observe the changes in stock detail, if have changes, set the stock in information and make changes on UI
        stockViewModel.stockDetail.observe(viewLifecycleOwner, Observer {
            adapter.addStockDetail(it)
            btn_stockDetailSave.isEnabled = adapter.itemCount > 0
        })

        // button "+" is clicked, go to add fragment
        btn_stockDetailAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddStockDetailFragment())
            transaction.addToBackStack("addStockDetailFragment")
            transaction.commit()
        }
        // when button "save" is click save the stock details
        btn_stockDetailSave.setOnClickListener {
            getProdsDetail()
        }
    }



    // get the current product details for update
    fun getProdsDetail(){
        readData(object : FirebaseCallback {
            override fun onCallBack(snapshot: DataSnapshot) {
                productSnapshot = snapshot
                Log.d("inside the on call back", snapshot.toString())
                val numbers = mutableListOf(stockViewModel.stocksDetail)
                var count: Int = 0
                // iterate through each product for update
                numbers.listIterator().forEach {
                    it.value?.forEach {
                        dbStockInDetail.push().setValue(it)
                        dbPermanentStock.push().setValue(it)
                        count += 1
                        stockUpdateProduct(it.stockDetailProdBarcode, it.stockDetailQty)
                        // calculate total price for each transaction
                        if (!(productfromDB?.prodPrice == null || it?.stockDetailQty == null)) {
                            var price: Double? = productfromDB?.prodPrice!!.toString().format("%.2f").toDoubleOrNull()
                            var qty: Int? = it?.stockDetailQty
                            var subtotal: Double = price!!.times(qty!!)
                            totalPrice = totalPrice.plus(subtotal)
                        }
                    }
                }
                // remove all the temporary stock detail (for adding purpose)
                dbTemp.removeValue()
                updateStockInDetail()
            }

        })
    }

    // update new stock detail transaction information and store to database
    private fun updateStockInDetail() {
        val stockIn = StockIn()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        var today = Calendar.getInstance().time
        var malaysiaTime = Calendar.getInstance()
        //malaysiaTime.add(Calendar.HOUR, 8)
        var todayTime =malaysiaTime.time
        stockIn.stockInDate = dateFormat.format(today)
        stockIn.stockInTime = timeFormat.format(todayTime)

        stockIn.stockInSupplierId = sharedStockInViewModel.stockInSupplierId.value
        stockIn.totalProdPrice = totalPrice.toString().format("%.2f").toDoubleOrNull()
        sharedStockInViewModel.addStockIn(stockIn)
        viewModelStore.clear()
        Toast.makeText(requireContext(), "Successfully Add Stock In", Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.popBackStack(getCallerFragment(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // update for each of the product which carry out stock in
    private fun stockUpdateProduct(stockDetailBarcode: String?, stockDetailQty: Int?){
        productSnapshot.children.forEach {
            var tempProd: Product? = null
            tempProd = it.getValue(Product::class.java)
            var prodDBBarcode: String = tempProd?.prodBarcode.toString()
            if(prodDBBarcode.equals(stockDetailBarcode)){
                productfromDB = it.getValue(Product::class.java)
                productfromDB?.prodQty = productfromDB?.prodQty?.plus(stockDetailQty!!)
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
        fun onCallBack(snapshot: DataSnapshot);
    }

    // get the previous fragment name
    fun getCallerFragment(): String? {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
        return fm.getBackStackEntryAt(count - 2).name
    }

    // when the "edit" button on the recycler view is click, go to edit stock detail fragment with passing parameter of stock detail
    override fun onRecyclerViewItemClicked(view: View, stockDetail: StockDetail) {
        when(view.id){
            R.id.btn_edit_stockDetail -> {
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, EditStockDetailFragment(stockDetail))
                transaction.addToBackStack("editStockDetailFragment")
                transaction.commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbTemp.removeValue()
    }
}

