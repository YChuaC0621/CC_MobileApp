package com.example.cc_mobileapp.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.google.firebase.database.FirebaseDatabase

// use to store scanned product barcode through shared view model
class ProductBarcodeViewModel: ViewModel() {

    // get and set of scanned barcode
    private val _scannedCode = MutableLiveData<String?>()
    val scannedCode: LiveData<String?>
        get() = _scannedCode

    fun productBarcode(retrievedCode :String){
        _scannedCode.value = retrievedCode
    }

    // clear scannned information after bind to user interface to allow next scanning
    fun clearBarcode(){
        _scannedCode.value = null
    }
}