package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import java.util.*

public class stock_in {
    private val stockIn_ID = MutableLiveData<Int>()
    val stockInID: MutableLiveData<Int>
        get() = stockIn_ID

    private val user_id = MutableLiveData<Int>()
    val userID: MutableLiveData<Int>
        get() = user_id

    private val stockIn_Date = MutableLiveData<Date>()
    val stockInDate: MutableLiveData<Date>
        get() = stockIn_Date
}