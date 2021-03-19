package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData

class supplier {
    private val supplier_id = MutableLiveData<Int>()
    val supplierID: MutableLiveData<Int>
        get() = supplier_id

    private val supplier_name = MutableLiveData<Int>()
    val supplierName: MutableLiveData<Int>
        get() = supplier_name

    private val cmp_name = MutableLiveData<String>()
    val cmpName: MutableLiveData<String>
        get() = cmp_name

    private val supplier_hpNum = MutableLiveData<String>()
    val supplierHpNumber: MutableLiveData<String>
        get() = supplier_hpNum

    private val supplier_email = MutableLiveData<String>()
    val supplierEmail: MutableLiveData<String>
        get() = supplier_email

    private val cmp_location = MutableLiveData<String>()
    val cmpLocation: MutableLiveData<String>
        get() = cmp_location

    private val service_status = MutableLiveData<Int>()
    val serviceStatus: MutableLiveData<Int>
        get() = service_status


}