package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude

data class Product(
    @get:Exclude
    var prodId: String? = null,
    var prodName: String? = null,
    var supplierId: String? = null,
    var prodDesc: String? = null,
    var prodBarcode: Int? = null,
    var prodQty: Int? = null,
    var prodPrice: Float? = null,
    @get:Exclude
    var isDeleted: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        return if(other is Product){
            other.prodId == prodId
        }else false
    }

    override fun hashCode(): Int {
        var result = prodId?.hashCode() ?: 0
        result = 31 * result + (prodName?.hashCode() ?: 0)
        result = 31 * result + (supplierId?.hashCode() ?: 0)
        result = 31 * result + (prodDesc?.hashCode() ?: 0)
        result = 31 * result + (prodBarcode ?: 0)
        result = 31 * result + (prodQty ?: 0)
        result = 31 * result + (prodPrice?.hashCode() ?: 0)
        return result
    }
}