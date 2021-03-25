package com.example.cc_mobileapp.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.Product
import com.google.firebase.database.FirebaseDatabase

class ProductViewModel: ViewModel() {
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addProduct(product: Product) {
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

}