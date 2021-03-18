package com.example.cc_mobileapp.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientViewModel: ViewModel()  {

    private val _clientName = MutableLiveData<String>()
    val clientName: LiveData<String>
        get() = _clientName

    private val _companyName = MutableLiveData<String>()
    val companyName: LiveData<String>
        get() = _companyName

    private val _email= MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _contactNo = MutableLiveData<String>()
    val contactNo: LiveData<String>
        get() = _contactNo

    private val _location = MutableLiveData<String>()
    val location: LiveData<String>
        get() = _location
}