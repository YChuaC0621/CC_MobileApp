package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData

public class rack {
    private val rack_ID = MutableLiveData<Int>()
    val rackID: MutableLiveData<Int>
        get() = rack_ID

    private val row_num = MutableLiveData<Int>()
    val rowNumber: MutableLiveData<Int>
        get() = row_num

    private val rack_location = MutableLiveData<String>()
    val rackLocation: MutableLiveData<String>
        get() = rack_location

    private val rack_barcode = MutableLiveData<String>()
    val rackBarcode: MutableLiveData<String>
        get() = rack_barcode
}