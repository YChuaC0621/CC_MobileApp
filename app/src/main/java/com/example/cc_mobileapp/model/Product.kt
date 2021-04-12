package com.example.cc_mobileapp.model

import com.google.firebase.database.Exclude

data class Product(
        @get:Exclude
        var prodId: String? = null,
        var prodName: String? = null,
        var supplierName: String? = null,
        var prodDesc: String? = null,
        var prodBarcode: Int? = null,
        var prodQty: Int? = 0,
        var prodPrice: Double? = 0.00,
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
        result = 31 * result + (supplierName?.hashCode() ?: 0)
        result = 31 * result + (prodDesc?.hashCode() ?: 0)
        result = 31 * result + (prodBarcode ?: 0)
        result = 31 * result + (prodQty ?: 0)
        result = 31 * result + (prodPrice?.hashCode() ?: 0)
        return result
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "prodId" to prodId,
                "prodName" to prodName,
                "supplierId" to supplierName,
                "prodDesc" to prodDesc,
                "prodBarcode" to prodBarcode,
                "prodQty" to prodQty,
                "prodPrice" to prodPrice
        )
    }
}