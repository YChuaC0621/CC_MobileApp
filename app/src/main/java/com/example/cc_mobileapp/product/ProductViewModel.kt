package com.example.cc_mobileapp.product

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant.NODE_PRODUCT
import com.example.cc_mobileapp.model.Product
import com.google.firebase.database.*

class ProductViewModel: ViewModel() {
    private val dbProduct = FirebaseDatabase.getInstance().getReference(NODE_PRODUCT)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result


    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products

    private var _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

//    private var _productFromDB = MutableLiveData<Product>()
//    val productFromDB: LiveData<Product>
//        get() = _productFromDB

    fun addProduct(product: Product) {
        Log.d("Check", "view model add prod $product")
        //create unique key
        product.prodId = dbProduct.push().key

    // save inside the unique key
    dbProduct.child(product.prodId!!).setValue(product)
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
            Log.d("Check", "productListener$snapshot")
            val product = snapshot.getValue(Product::class.java)
            product?.prodId = snapshot.key
            _product.value = product
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val product = snapshot.getValue(Product::class.java)
            product?.prodId = snapshot.key
            _product.value = product
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("Check", "onchildremove $snapshot")

            val product = snapshot.getValue(Product::class.java)
            product?.prodId = snapshot.key
            product?.isDeleted = true
            _product.value = product
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbProduct.addChildEventListener(childEventListener)
    }

    fun fetchProduct(){
        dbProduct.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch products")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val products = mutableListOf<Product>()
                    for(productSnapshot in snapshot.children){
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.prodId = productSnapshot.key
                        product?.let {products.add(it)}
                    }
                    _products.value = products
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateProduct(product: Product){
        dbProduct.child(product.prodId!!).setValue(product)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    fun deleteProduct(product: Product){
        dbProduct.child(product.prodId!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        dbProduct.removeEventListener(childEventListener)
    }


}