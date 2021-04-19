package com.example.cc_mobileapp.model

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude

data class Client (
    @get:Exclude
    // client construction
    var clientId: String? = null,
    var clientCoName: String? = null,
    var clientEmail: String? = null,
    var clientHpNum: String? = null,
    var clientLocation: String? = null,
    @get:Exclude
    var isDeleted: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        return if(other is Client){
            other.clientId ==clientId
        }else false
    }

    override fun hashCode(): Int {
        var result = clientId?.hashCode() ?: 0
        result = 31 * result + (clientCoName?.hashCode() ?: 0)
        result = 31 * result + (clientEmail?.hashCode() ?: 0)
        result = 31 * result + (clientHpNum?.hashCode() ?: 0)
        result = 31 * result + (clientLocation?.hashCode() ?: 0)
        return result
    }
}

    //    public class client(){
//
//    }
//
//    private val client_id = MutableLiveData<Int>()
//    val clientID: MutableLiveData<Int>
//        get() = client_id
//
//    private val client_companyName = MutableLiveData<String>()
//    val clientName: MutableLiveData<String>
//        get() = client_companyName
//
//    private val client_email = MutableLiveData<String>()
//    val clientEmail: MutableLiveData<String>
//        get() = client_email
//
//    private val client_hpNum = MutableLiveData<String>()
//    val clientHpNumber: MutableLiveData<String>
//        get() = client_hpNum
//
//    private val client_location = MutableLiveData<String>()
//    val clientLocation: MutableLiveData<String>
//        get() = client_location

