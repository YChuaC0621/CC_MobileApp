package com.example.cc_mobileapp.stock.stockDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.google.firebase.database.FirebaseDatabase

class StockBarcodeViewModel: ViewModel() {

    private val _scannedProductCode = MutableLiveData<String?>()
    val scannedProductCode: LiveData<String?>
        get() = _scannedProductCode

    private val _scannedRackCode = MutableLiveData<String?>()
    val scannedRackCode: LiveData<String?>
        get() = _scannedRackCode

    fun productBarcode(retrievedCode :String){
        _scannedProductCode.value = retrievedCode
    }

    fun rackBarcode(retrievedCode :String){
        _scannedRackCode.value = retrievedCode
    }

    fun clearBarcode(){
        _scannedProductCode.value = null
        _scannedRackCode.value = null
    }
}