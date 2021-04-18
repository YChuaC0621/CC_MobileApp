package com.example.cc_mobileapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude

data class Users (
    @get:Exclude
    var userId: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userHpNum: String? = null,
    var userPsw: String? = null,
    var workingStatus: Int? = null,
    var workingPosition: Int? = null)
{

    override fun equals(other: Any?): Boolean {
        return if(other is Users){
            other.userId == userId
        }else false
    }

    override fun hashCode(): Int {
        var result = userName?.hashCode() ?: 0
        result = 31 * result + (userEmail?.hashCode() ?: 0)
        result = 31 * result + (userHpNum?.hashCode() ?: 0)
        result = 31 * result + (userPsw?.hashCode() ?: 0)
        result = 31 * result + (workingStatus ?: 0)
        result = 31 * result + (workingPosition ?: 0)
        return result
    }
}






