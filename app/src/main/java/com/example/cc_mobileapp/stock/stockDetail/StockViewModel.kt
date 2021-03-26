package com.example.cc_mobileapp.stock.stockDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.StockDetail
import com.google.firebase.database.*

class StockViewModel : ViewModel() {
    private val dbStockDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _stocksDetail = MutableLiveData<List<StockDetail>>()
    val stocksDetail: LiveData<List<StockDetail>>
        get() = _stocksDetail

    private val _stockDetail = MutableLiveData<StockDetail>()
    val stockDetail: LiveData<StockDetail>
        get() = _stockDetail

    fun addStockDetail(stockDetail: StockDetail) {
        Log.d("Check", "view model add prod $stockDetail")
        //create unique key
        // TODO if stock in then another push key
        stockDetail.stockDetailId = dbStockDetail.push().key

        // save inside the unique key
        dbStockDetail.child(stockDetail.stockDetailId!!).setValue(stockDetail)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    private val childEventListener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "StockDetailListener$snapshot")
            val stockDetail = snapshot.getValue(StockDetail::class.java)
            stockDetail?.stockDetailId = snapshot.key
            _stockDetail.value = stockDetail
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbStockDetail.addChildEventListener(childEventListener)
    }

//    fun fetchStockDetail(){
//        dbStockDetail.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("Check", "fetch stock details")
//                if(snapshot.exists()){
//                    Log.d("Check", "has snapshot $snapshot")
//                    val stocksDetail = mutableListOf<StockDetail>()
//                    for(stockDetailSnapshot in snapshot.children){
//                        val stockDetail = stockDetailSnapshot.getValue(StockDetail::class.java)
//                        stockDetail?.stockDetailId = stockDetailSnapshot.key
//                        stockDetail?.let {stocksDetail.add(it)}
//                    }
//                    _stocksDetail.value = stocksDetail
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

    override fun onCleared() {
        super.onCleared()
        dbStockDetail.removeEventListener(childEventListener)
    }

}