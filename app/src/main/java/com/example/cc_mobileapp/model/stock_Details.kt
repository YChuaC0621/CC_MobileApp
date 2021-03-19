package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import java.util.*

public class stock_Details
{
    private val stockDetails_ID = MutableLiveData<Int>()
    val stockDetailsID: MutableLiveData<Int>
        get() = stockDetails_ID

    private val prod_id = MutableLiveData<Int>()
    val prodID: MutableLiveData<Int>
        get() = prod_id

    private val stock_id = MutableLiveData<Int>()
    val stockID: MutableLiveData<Int>
        get() = stock_id


    private val rack_id = MutableLiveData<Int>()
    val rackID: MutableLiveData<Int>
        get() = rack_id

    private val row_num = MutableLiveData<Int>()
    val rowNumber: MutableLiveData<Int>
        get() = row_num

    private val qty = MutableLiveData<Int>()
    val quantity: MutableLiveData<Int>
        get() = qty
}