package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData

class product {
    private val prod_id = MutableLiveData<Int>()
    val prodID: MutableLiveData<Int>
        get() = prod_id

    private val supplier_id = MutableLiveData<Int>()
    val supplierID: MutableLiveData<Int>
        get() = supplier_id

    private val prod_name = MutableLiveData<String>()
    val prodName: MutableLiveData<String>
        get() = prod_name

    private val prod_desc = MutableLiveData<String>()
    val prodDesc: MutableLiveData<String>
        get() = prod_desc

    private val prod_barcode = MutableLiveData<String>()
    val prodBarcode: MutableLiveData<String>
        get() = prod_barcode

    private val prod_qty = MutableLiveData<Int>()
    val prodQty: MutableLiveData<Int>
        get() = prod_qty

    private val prod_price = MutableLiveData<Float>()
    val prodPrice: MutableLiveData<Float>
        get() = prod_price
}