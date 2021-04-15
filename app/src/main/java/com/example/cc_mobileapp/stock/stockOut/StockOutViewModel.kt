package com.example.cc_mobileapp.stock.stockOut

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.model.StockOut
import com.google.firebase.database.*

class StockOutViewModel: ViewModel() {
    private val dbStockOut = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUT)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _stocksOut = MutableLiveData<List<StockOut>>()
    val stocksOut: LiveData<List<StockOut>>
        get() = _stocksOut

    private val _stockOut = MutableLiveData<StockOut>()
    val stockOut: LiveData<StockOut>
        get() = _stockOut

    private val _totalPrice = MutableLiveData<StockOut>()
    val totalPrice: LiveData<StockOut>
        get() = _totalPrice

    private val _totalNoStockOutDetail = MutableLiveData<StockOut>()
    val totalNoStockOutDetail: LiveData<StockOut>
        get() = _totalNoStockOutDetail

    private val _stockOutTypePushKey = MutableLiveData<String>()
    val stockOutTypePushKey: LiveData<String>
        get() = _stockOutTypePushKey

    private val _stockOutClientId = MutableLiveData<String>()
    val stockOutClientId: LiveData<String>
        get() = _stockOutClientId

    fun generateTypePuskKey(){
        _stockOutTypePushKey.value = dbStockOut.push().key
    }

    fun addStockOut(stockOut: StockOut) {
        Log.d("Check", "view model add stock out $stockOut")
        //create unique key
        // TODO if stock out then another push key
        stockOut.stockOutId = stockOutTypePushKey.value
        // save inside the unique key
        dbStockOut.child(stockOut.stockOutId!!).setValue(stockOut)
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
            Log.d("Check", "StockOutListener$snapshot")
            val stockOut = snapshot.getValue(StockOut::class.java)
            stockOut?.stockOutId = snapshot.key
            _stockOut.value = stockOut!!
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
        dbStockOut.addChildEventListener(childEventListener)
    }

    fun fetchStockOut(){
        dbStockOut.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch stock details")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val stocksOut = mutableListOf<StockOut>()
                    for(stockOutSnapshot in snapshot.children){
                        val stockOut = stockOutSnapshot.getValue(StockOut::class.java)
                        stockOut?.stockOutId = stockOutSnapshot.key
                        stockOut?.let {stocksOut.add(it)}
                    }
                    _stocksOut.value = stocksOut
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        dbStockOut.removeEventListener(childEventListener)
    }

    fun setStockOutClientId(stockOutClientId:String){
        _stockOutClientId.value = stockOutClientId
    }
}