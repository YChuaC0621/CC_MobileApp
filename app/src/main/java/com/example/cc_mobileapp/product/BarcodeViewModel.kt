package com.example.cc_mobileapp.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.google.firebase.database.FirebaseDatabase

class BarcodeViewModel: ViewModel() {

    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)


    private val _scannedCode = MutableLiveData<String?>()
    val scannedCode: LiveData<String?>
        get() = _scannedCode

    fun productBarcode(retrievedCode :String){
        _scannedCode.value = retrievedCode
    }

    fun clearBarcode(){
        _scannedCode.value = null
    }
}