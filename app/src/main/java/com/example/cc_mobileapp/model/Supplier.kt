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
    @get:Exclude
    var supStatus: Boolean = true
    )
{
    override fun equals(other: Any?): Boolean {
        return if(other is Supplier){
            other.supId == supId
        }else false
    }

    override fun hashCode(): Int {
        var result = supId?.hashCode() ?: 0
        result = 31 * result + (supName?.hashCode() ?: 0)
        result = 31 * result + (supEmail?.hashCode() ?: 0)
        result = 31 * result + (supHpNum?.hashCode() ?: 0)
        result = 31 * result + (supCmpName?.hashCode() ?: 0)
        result = 31 * result + (supCmpLot?.hashCode() ?: 0)
        return result
    }
}