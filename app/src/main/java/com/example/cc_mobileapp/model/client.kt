package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData

public class client {
    private val client_id = MutableLiveData<Int>()
    val clientID: MutableLiveData<Int>
        get() = client_id

    private val client_name = MutableLiveData<String>()
    val clientName: MutableLiveData<String>
        get() = client_name

    private val client_hpNum = MutableLiveData<String>()
    val clientHpNumber: MutableLiveData<String>
        get() = client_hpNum

    private val client_Email = MutableLiveData<String>()
    val clientEmail: MutableLiveData<String>
        get() = client_Email

    private val client_location = MutableLiveData<String>()
    val clientLocation: MutableLiveData<String>
        get() = client_location

}
