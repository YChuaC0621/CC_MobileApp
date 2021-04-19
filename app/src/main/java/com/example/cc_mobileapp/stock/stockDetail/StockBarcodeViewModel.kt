package com.example.cc_mobileapp.stock.stockDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.google.firebase.database.FirebaseDatabase

// use to store scanned product barcode through shared view model
class StockBarcodeViewModel: ViewModel() {
    // get and set of scanned barcode
    private val _scannedProductCode = MutableLiveData<String?>()
    val scannedProductCode: LiveData<String?>
        get() = _scannedProductCode

    fun productBarcode(retrievedCode :String){
        _scannedProductCode.value = retrievedCode
    }

    // clear scannned information after bind to user interface to allow next scanning
    fun clearBarcode(){
        _scannedProductCode.value = null
    }
}