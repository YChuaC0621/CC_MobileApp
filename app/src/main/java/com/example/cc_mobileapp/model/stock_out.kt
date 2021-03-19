package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import java.util.*

public class stock_out {
    private val stockOut_ID = MutableLiveData<Int>()
    val stockOutID: MutableLiveData<Int>
        get() = stockOut_ID

    private val client_id = MutableLiveData<Int>()
    val clientID: MutableLiveData<Int>
        get() = client_id

    private val stockOut_Date = MutableLiveData<Date>()
    val stockOutDate: MutableLiveData<Date>
        get() = stockOut_Date
}