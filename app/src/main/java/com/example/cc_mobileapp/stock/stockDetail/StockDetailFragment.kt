package com.example.cc_mobileapp.stock.stockDetail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.util.*
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList
import kotlin.time.times

class StockDetailFragment : Fragment(), StockDetailRecyclerViewClickListener {

    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbTemp = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private lateinit var productViewModel: ProductViewModel
    private val adapter = StockDetailAdapter()
    private lateinit var stockViewModel: StockViewModel
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private lateinit var rackViewModel: RackViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        productViewModel = ViewModelProvider(this@StockDetailFragment).get(ProductViewModel::class.java)
        stockViewModel = ViewModelProvider(this@StockDetailFragment).get(StockViewModel::class.java)
        rackViewModel = ViewModelProvider(this@StockDetailFragment).get(RackViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stock_detail, container, false)
    }

    lateinit var prodList: ArrayList<Product>

    private fun retrieveProdDB(firebaseCallback: FirebaseCallback) {
        var prodPriceQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_SUPPLIER)
        prodPriceQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var prod: Product? = snapshot.getValue(Product::class.java)
                prodList.add(prod!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        getProd()
    }

    private fun getProd(){
        retrieveProdDB(object:FirebaseCallback{
            override fun onCallBack(snapshot: DataSnapshot) {
                returnProd()
            }
        })
    }

    private fun returnProd(): ArrayList<Product>{
        return prodList
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_stockDetail.adapter = adapter

        productViewModel.fetchProduct()

        stockViewModel.fetchStockDetail()

        stockViewModel.getRealtimeUpdates()

        stockViewModel.stocksDetail.observe(viewLifecycleOwner, Observer{
            adapter.setStocksDetail(it)
            btn_stockDetailSave.isEnabled = adapter.itemCount > 0
        })

        stockViewModel.stockDetail.observe(viewLifecycleOwner, Observer{
            adapter.addStockDetail(it)
            btn_stockDetailSave.isEnabled = adapter.itemCount > 0
        })

        btn_stockDetailAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddStockDetailFragment())
            transaction.addToBackStack("addStockDetailFragment")
            transaction.commit()
        }


        btn_stockDetailSave.setOnClickListener {
            getProdsDetail()
        }
    }

    private lateinit var productSnapshot: DataSnapshot
    private var totalPrice: Double = 0.0

    fun getProdsDetail(){
        readData(object : FirebaseCallback{
            override fun onCallBack(snapshot: DataSnapshot) {
                productSnapshot = snapshot
                Log.d("inside the on call back", snapshot.toString())
                val numbers = mutableListOf(stockViewModel.stocksDetail)
                var count: Int = 0
                numbers.listIterator().forEach {
                    it.value?.forEach {
                        it.stockStatus = true
                        dbStockInDetail.push().setValue(it)
                        count+=1
                        stockUpdateProduct(it.stockDetailProdBarcode, it.stockDetailQty)
                        if(!(productfromDB?.prodPrice == null || it?.stockDetailQty == null)){
                            var price: Double = productfromDB?.prodPrice!!
                            var qty : Int? = it?.stockDetailQty
                            var subtotal:Double = price.times(qty!!)
                            totalPrice = totalPrice.plus(subtotal)
                        }
                    }
                }
                dbTemp.removeValue()
                updateStockInDetail()
            }

        })
    }

    private fun updateStockInDetail() {
        val stockIn = StockIn()
        var today = Calendar.getInstance()
        today.add(Calendar.HOUR,8)
        stockIn.stockInDateTime = today.time.toString()
        stockIn.stockInSupplierId = sharedStockInViewModel.stockInSupplierId.value
        stockIn.totalProdPrice = totalPrice
        sharedStockInViewModel.addStockIn(stockIn)
        viewModelStore.clear()
        requireActivity().supportFragmentManager.popBackStack(getCallerFragment(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


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
                //subtotal = (prodQty!! * product?.prodPrice!!).toDouble()
            }
        }
    }


    var productfromDB: Product? = null
    var callBackCount: Int = 0

    private fun getData(snapshot: DataSnapshot, prodBarcode: Int) {
        snapshot.children.forEach {
            var product = it.getValue(Product::class.java)!!
            if(product.prodBarcode!!.equals(prodBarcode)){
                Log.d("checkStoreprodBarcode", product.prodBarcode.toString())
                productfromDB = it.getValue(Product::class.java)
                Log.d("checkStoreafterStore", it.value.toString())
            }
            Log.d("checkStoreForEach", product.toString())
        }
    }

    private fun readData(firebaseCallback: FirebaseCallback){
        dbProduct.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(callBackCount ==0){
                    firebaseCallback.onCallBack(snapshot)
                    callBackCount+=1
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private interface FirebaseCallback{
        fun onCallBack(snapshot: DataSnapshot);
    }

//    var totalPrice = stockViewModel.storedIntoStockInDB()
//

    fun getCallerFragment(): String? {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
        Toast.makeText(requireContext(), fm.getBackStackEntryAt(count - 2).name, Toast.LENGTH_SHORT).show()
        return fm.getBackStackEntryAt(count - 2).name
    }

    override fun onRecyclerViewItemClicked(view: View, stockDetail: StockDetail) {
        when(view.id){
            R.id.btn_edit_stockDetail -> {
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, EditStockDetailFragment(stockDetail))
                transaction.addToBackStack("editStockDetailFragment")
                transaction.commit()
            }
            R.id.btn_delete_stockDetail ->{
                AlertDialog.Builder(requireContext()).also{
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)){ dialog, which ->
                        stockViewModel.deleteStockDetail(stockDetail)
                    }
                    it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
                }.create().show()
            }
        }
    }
}

