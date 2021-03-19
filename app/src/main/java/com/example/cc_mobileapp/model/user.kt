package com.example.cc_mobileapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

public class user {
    private val user_id = MutableLiveData<Int>()
    val userID: MutableLiveData<Int>
        get() = user_id

    private val user_name = MutableLiveData<String>()
    val userName: MutableLiveData<String>
        get() = user_name

    private val user_hpNum = MutableLiveData<String>()
    val userHpNumber: MutableLiveData<String>
        get() = user_hpNum

    private val user_email = MutableLiveData<String>()
    val userEmail: MutableLiveData<String>
        get() = user_email

    private val psw = MutableLiveData<String>()
    val userPsw: MutableLiveData<String>
        get() = psw

    private val working_status = MutableLiveData<Int>()
    val workingStatus: MutableLiveData<Int>
        get() = working_status

    private val working_pos = MutableLiveData<Int>()
    val workingPos: MutableLiveData<Int>
        get() = working_pos







}