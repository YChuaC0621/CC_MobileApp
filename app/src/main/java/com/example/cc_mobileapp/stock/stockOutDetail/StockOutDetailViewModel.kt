package com.example.cc_mobileapp.stock.stockOutDetail

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockOut
import com.example.cc_mobileapp.model.StockOutDetail
import com.example.cc_mobileapp.product.ProductViewModel
import com.google.firebase.database.*

class StockOutDetailViewModel : ViewModel() {
    // variable declaration
    private val dbStockOutDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUTDETAIL)
    private val dbTempOut = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP_OUT)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _stocksOutDetail = MutableLiveData<List<StockOutDetail>>()
    val stocksOutDetail: LiveData<List<StockOutDetail>>
        get() = _stocksOutDetail

    private val _stockOutDetail = MutableLiveData<StockOutDetail>()
    val stockOutDetail: LiveData<StockOutDetail>
        get() = _stockOutDetail

    private val _stockOutTypeKey = MutableLiveData<String>()
    val stockOutTypeKey: LiveData<String>
        get() = _stockOutTypeKey

    // add stock out detail
    fun addStockOutDetail(stockOutDetail: StockOutDetail) {
        //create unique key
        // TODO if stock in then another push key
        stockOutDetail.stockOutDetailId = dbTempOut.push().key

        // save inside the unique key
        dbTempOut.child(stockOutDetail.stockOutDetailId!!).setValue(stockOutDetail)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }
    // check for realtime changes
    private val childEventListener = object : ChildEventListener {
        // add stock detail
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "StockOutDetailListener$snapshot")
            val stockOutDetail = snapshot.getValue(StockOutDetail::class.java)
            stockOutDetail?.stockOutDetailId = snapshot.key
            _stockOutDetail.value = stockOutDetail!!
        }
        // update stock detail
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val stockOutDetail = snapshot.getValue(StockOutDetail::class.java)
            stockOutDetail?.stockOutDetailId = snapshot.key
            _stockOutDetail.value = stockOutDetail!!
        }
        // delete stock detail
        override fun onChildRemoved(snapshot: DataSnapshot) {
            val stockOutDetail = snapshot.getValue(StockOutDetail::class.java)
            stockOutDetail?.stockOutDetailId = snapshot.key
            stockOutDetail?.isDeleted = true
            _stockOutDetail.value = stockOutDetail!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
    // get realtime updates
    fun getRealtimeUpdates() {
        dbTempOut.addChildEventListener(childEventListener)
    }

    // get stock out detail information from database
    fun fetchStockOutDetail() {
        dbTempOut.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val stocksOutDetail = mutableListOf<StockOutDetail>()
                    for (stockOutDetailSnapshot in snapshot.children) {
                        val stockOutDetail = stockOutDetailSnapshot.getValue(StockOutDetail::class.java)
                        stockOutDetail?.stockOutDetailId = stockOutDetailSnapshot.key
                        stockOutDetail?.let { stocksOutDetail.add(it) }
                    }
                    _stocksOutDetail.value = stocksOutDetail
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // get stock out detail information from database with specific stockTypeId
    fun fetchStockOutDetailDisplay(stockOutID: String) {
        dbStockOutDetail.orderByChild("stockTypeId").equalTo(stockOutID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val stocksOutDetail = mutableListOf<StockOutDetail>()
                    for (stockOutDetailSnapshot in snapshot.children) {
                        val stockOutDetail = stockOutDetailSnapshot.getValue(StockOutDetail::class.java)
                        stockOutDetail?.stockOutDetailId = stockOutDetailSnapshot.key
                        stockOutDetail?.let { stocksOutDetail.add(it) }
                    }
                    _stocksOutDetail.value = stocksOutDetail
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
        dbTempOut.removeEventListener(childEventListener)
        dbStockOutDetail.removeEventListener(childEventListener)
    }

    // update the stock out detail
    fun updateStockOutDetail(stockOutDetail: StockOutDetail) {
        dbTempOut.child(stockOutDetail.stockOutDetailId!!).setValue(stockOutDetail)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    // delete the stock out detail
    fun deleteStockOutDetail(stockOutDetail: StockOutDetail) {
        dbTempOut.child(stockOutDetail.stockOutDetailId!!).setValue(null)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }



}
