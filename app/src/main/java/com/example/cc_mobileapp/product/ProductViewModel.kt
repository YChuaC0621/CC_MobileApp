package com.example.cc_mobileapp.product

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.Constant.NODE_PRODUCT
import com.example.cc_mobileapp.model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*

class ProductViewModel: ViewModel() {
    // variable declaration
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

    private val _validInput = MutableLiveData<Boolean>()
    val validInput: LiveData<Boolean>
        get() = _validInput

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
    // check for realtime changes
    private val childEventListener = object: ChildEventListener {
        // add product
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "productListener$snapshot")
            val product = snapshot.getValue(Product::class.java)
            product?.prodId = snapshot.key
            _product.value = product!!
        }
        // update product information
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val product = snapshot.getValue(Product::class.java)
            product?.prodId = snapshot.key
            _product.value = product!!
        }
        // delete product
        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("Check", "onchildremove $snapshot")

            val product = snapshot.getValue(Product::class.java)
            product?.prodId = snapshot.key
            product?.isDeleted = true
            _product.value = product!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    // get realtime update on changes
    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbProduct.addChildEventListener(childEventListener)
    }

    // retrieve all the products from database
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

    // update product to database
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

    // delete product from database
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

    // clear the listener from fragment to product database
    override fun onCleared() {
        super.onCleared()
        dbProduct.removeEventListener(childEventListener)
    }


}