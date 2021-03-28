package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.example.cc_mobileapp.Constant
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

class Supplier (
    @get:Exclude
    var supId: String? = null,
    var supName: String? = null,
    var supEmail: String? = null,
    var supHpNum: String? = null,
    var supCmpName: String? = null,
    var supCmpLot: String? = null,
    var supStatus: Boolean = true
    )
{
}