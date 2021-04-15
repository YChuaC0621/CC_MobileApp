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

    private val _reports = MutableLiveData<List<Product>>()
    val reports: LiveData<List<Product>>
        get() = _reports



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
}