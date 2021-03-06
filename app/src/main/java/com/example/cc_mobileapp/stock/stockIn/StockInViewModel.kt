package com.example.cc_mobileapp.stock.stockIn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.StockIn
import com.google.firebase.database.*

class StockInViewModel: ViewModel() {
    // variable declaration with get and set
    private val dbStockIn = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKIN)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _stocksIn = MutableLiveData<List<StockIn>>()
    val stocksIn: LiveData<List<StockIn>>
        get() = _stocksIn

    private val _stockIn = MutableLiveData<StockIn>()
    val stockIn: LiveData<StockIn>
        get() = _stockIn

    private val _totalCost = MutableLiveData<StockIn>()
    val totalCost: LiveData<StockIn>
        get() = _totalCost

    private val _totalNoStockDetail = MutableLiveData<StockIn>()
    val totalNoStockDetail: LiveData<StockIn>
        get() = _totalNoStockDetail

    private val _stockTypePushKey = MutableLiveData<String>()
    val stockTypePushKey: LiveData<String>
        get() = _stockTypePushKey

    private val _stockInSupplierId = MutableLiveData<String>()
    val stockInSupplierId: LiveData<String>
        get() = _stockInSupplierId

    // generate key id for stock in
    fun generateTypePushKey(){
        _stockTypePushKey.value = dbStockIn.push().key
    }

    // add stock in
    fun addStockIn(stockIn: StockIn) {
        Log.d("Check", "view model add stock in $stockIn")
        //create unique key
        stockIn.stockInId = stockTypePushKey.value
        // save inside the unique key
        dbStockIn.child(stockIn.stockInId!!).setValue(stockIn)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    // check for realtime changes
    private val childEventListener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            // add stock in
            Log.d("Check", "StockInListener$snapshot")
            val stockIn = snapshot.getValue(StockIn::class.java)
            stockIn?.stockInId = snapshot.key
            _stockIn.value = stockIn!!
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

    // get real time updates
    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbStockIn.addChildEventListener(childEventListener)
    }
    // get stock in information from database
    fun fetchStockIn(){
        dbStockIn.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch stock details")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val stocksIn = mutableListOf<StockIn>()
                    for(stockInSnapshot in snapshot.children){
                        val stockIn = stockInSnapshot.getValue(StockIn::class.java)
                        stockIn?.stockInId = stockInSnapshot.key
                        stockIn?.let {stocksIn.add(it)}
                    }
                    _stocksIn.value = stocksIn
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    // remove the listener when the fragment is cleared
    override fun onCleared() {
        super.onCleared()
        dbStockIn.removeEventListener(childEventListener)
    }

    // set the stock in supplier id
    fun setStockInSupplierId(stockInSupplierId:String){
        _stockInSupplierId.value = stockInSupplierId
    }
}