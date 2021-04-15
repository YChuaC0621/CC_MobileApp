package com.example.cc_mobileapp.stock.stockOutDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.google.firebase.database.FirebaseDatabase

class StockOutBarcodeViewModel: ViewModel() {

    private val _scannedProductCode = MutableLiveData<String?>()
    val scannedProductCode: LiveData<String?>
        get() = _scannedProductCode

    fun productBarcode(retrievedCode :String){
        _scannedProductCode.value = retrievedCode
    }

    fun clearBarcode(){
        _scannedProductCode.value = null
    }
}