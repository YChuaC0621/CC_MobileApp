package com.example.cc_mobileapp.stock.stockDetail

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.product.ProductViewModel
import com.google.firebase.database.*

class StockViewModel : ViewModel() {
    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbTemp = FirebaseDatabase.getInstance().getReference(Constant.NODE_TEMP)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)


    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _stocksDetail = MutableLiveData<List<StockDetail>>()
    val stocksDetail: LiveData<List<StockDetail>>
        get() = _stocksDetail

    private val _stockDetail = MutableLiveData<StockDetail>()
    val stockDetail: LiveData<StockDetail>
        get() = _stockDetail

    private val _stockTypeKey = MutableLiveData<String>()
    val stockTypeKey: LiveData<String>
        get() = _stockTypeKey

    fun addStockDetail(stockDetail: StockDetail) {
        //create unique key
        // TODO if stock in then another push key
        stockDetail.stockDetailId = dbTemp.push().key

        // save inside the unique key
        dbTemp.child(stockDetail.stockDetailId!!).setValue(stockDetail)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "StockDetailListener$snapshot")
            val stockDetail = snapshot.getValue(StockDetail::class.java)
            stockDetail?.stockDetailId = snapshot.key
            _stockDetail.value = stockDetail
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val stockDetail = snapshot.getValue(StockDetail::class.java)
            stockDetail?.stockDetailId = snapshot.key
            _stockDetail.value = stockDetail
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val stockDetail = snapshot.getValue(StockDetail::class.java)
            stockDetail?.stockDetailId = snapshot.key
            stockDetail?.isDeleted = true
            _stockDetail.value = stockDetail
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealtimeUpdates() {
        dbTemp.addChildEventListener(childEventListener)
    }

    fun fetchStockDetail() {
        dbTemp.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val stocksDetail = mutableListOf<StockDetail>()
                    for (stockDetailSnapshot in snapshot.children) {
                        val stockDetail = stockDetailSnapshot.getValue(StockDetail::class.java)
                        stockDetail?.stockDetailId = stockDetailSnapshot.key
                        stockDetail?.let { stocksDetail.add(it) }
                    }
                    _stocksDetail.value = stocksDetail
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        dbTemp.removeEventListener(childEventListener)
        dbStockInDetail.removeEventListener(childEventListener)
    }

    fun updateStockDetail(stockDetail: StockDetail) {
        dbTemp.child(stockDetail.stockDetailId!!).setValue(stockDetail)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    fun deleteStockDetail(stockDetail: StockDetail) {
        dbTemp.child(stockDetail.stockDetailId!!).setValue(null)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    fun storedIntoStockInDB(): Double? {
        val numbers = mutableListOf(stocksDetail)
        var count: Int = 0
        var totalPrice: Double = 0.00
        numbers.listIterator().forEach {
            var product: Product?
            it.value?.forEach {
                dbStockInDetail.push().setValue(it)
                count+=1
                product = stockUpdateProduct(it.stockDetailProdBarcode.toString(), it.stockDetailQty)
                if(product?.prodPrice != null && it?.stockDetailQty != null){
                    var price: Double = product?.prodPrice.toString().toDouble()
                    var qty : Double = it?.stockDetailQty.toString().toDouble()
                    var subtotal:Double = price.times(qty)
                    totalPrice = totalPrice.plus(subtotal.toDouble())
                }
            }
        }
        return totalPrice
        dbTemp.removeValue()
    }

    fun stockUpdateProduct(productBarcode: String, prodQty: Int?): Product? {
        dbProduct.get().addOnSuccessListener{
            Log.d("check", "success Query")
            //var subtotal: Double
            if(it.exists()){
                it.children.forEach{ it ->
                    var product: Product? = it.getValue(Product::class.java)
                    //subtotal = 0.00
                    if(product?.prodBarcode.toString() == productBarcode)
                    {
                        product?.prodQty = prodQty
                        dbProduct.child(it.key!!).setValue(product)
                                .addOnCompleteListener { it ->
                                    if (it.isSuccessful) {
                                        Log.d("check", "update successfully")
                                    } else {
                                        Log.d("check", "update successfully")
                                    }
                                }
                        //subtotal = (prodQty!! * product?.prodPrice!!).toDouble()
                        return@forEach
                    }
                }
            }
            else{
                Log.d("check", "query does not contain any")
            }
        }.addOnCanceledListener {
            Log.d("check", "cancelled")
        }.addOnFailureListener {
            Log.d("check", "fail")
        }
        return null
    }

}
