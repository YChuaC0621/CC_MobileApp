package com.example.cc_mobileapp.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReportViewModel(): ViewModel() {
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private val dbStockIn = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKIN)
    private val dbStockOut = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUT)
    private val dbStockOutDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUTDETAIL)
    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_PERM_STOCKINDETAIL)

    private val _reports = MutableLiveData<List<Product>>()
    val reports: LiveData<List<Product>>
        get() = _reports

    private val _stockin_reports = MutableLiveData<List<StockIn>>()
    val stockin_reports: LiveData<List<StockIn>>
        get() = _stockin_reports

    private val _stockout_reports = MutableLiveData<List<StockOut>>()
    val stockout_reports: LiveData<List<StockOut>>
        get() = _stockout_reports

    private val _stockoutdetails_reports = MutableLiveData<List<StockOutDetail>>()
    val stockoutdetails_reports: LiveData<List<StockOutDetail>>
        get() = _stockoutdetails_reports

    private val _stockindetails_reports = MutableLiveData<List<StockDetail>>()
    val stockindetails_reports: LiveData<List<StockDetail>>
        get() = _stockindetails_reports



    fun fetchProdReportDetails() {
        dbProduct.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch product")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val prods = mutableListOf<Product>()
                    for(prodSnapshot in snapshot.children){
                        val prod = prodSnapshot.getValue(Product::class.java)
                        prod?.prodId = prodSnapshot.key
                        prod?.let {prods.add(it)}
                    }
                    _reports.value = prods
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun fetchStockInReportDetails() {
        dbStockIn.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch stockin")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val stockinList = mutableListOf<StockIn>()
                    for(stockinSnapshot in snapshot.children){
                        val stockin = stockinSnapshot.getValue(StockIn::class.java)
                        stockin?.stockInId = stockinSnapshot.key
                        stockin?.let {stockinList.add(it)}
                    }
                    _stockin_reports.value = stockinList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun fetchStockOutReportDetails() {
        dbStockOut.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch stockout")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val stockoutList = mutableListOf<StockOut>()
                    for(stockoutSnapshot in snapshot.children){
                        val stockout = stockoutSnapshot.getValue(StockOut::class.java)
                        stockout?.stockOutId = stockoutSnapshot.key
                        stockout?.let {stockoutList.add(it)}
                    }
                    _stockout_reports.value = stockoutList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun fetchStockOutDetailReportDetails() {
        dbStockOutDetail.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch stockout details")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val stockoutdetailList = mutableListOf<StockOutDetail>()
                    for(stockoutdetailsSnapshot in snapshot.children){
                        val stockoutdetail = stockoutdetailsSnapshot.getValue(StockOutDetail::class.java)
                        stockoutdetail?.stockOutDetailId = stockoutdetailsSnapshot.key
                        stockoutdetail?.let {stockoutdetailList.add(it)}
                    }
                    _stockoutdetails_reports.value = stockoutdetailList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun fetchStockInDetailReportDetails() {
        dbStockInDetail.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch stockin details")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val stockindetailList = mutableListOf<StockDetail>()
                    for(stockdetailsSnapshot in snapshot.children){
                        val stockdetail = stockdetailsSnapshot.getValue(StockDetail::class.java)
                        stockdetail?.stockDetailId = stockdetailsSnapshot.key
                        stockdetail?.let {stockindetailList.add(it)}
                    }
                    _stockindetails_reports.value = stockindetailList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}