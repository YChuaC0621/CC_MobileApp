package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude
import java.util.*

data class StockIn (
    @get:Exclude
    var stockInId: String? = null,
    var stockInDate: String? = null,
    var stockInTime: String? = null,
    var stockInSupplierId: String? = null,
    var totalProdPrice: Double? = 0.00
    )
    {
        override fun equals(other: Any?): Boolean
        {
            return if (other is StockIn)
            {
                other.stockInId == stockInId
            } else false
        }

        override fun hashCode(): Int {
            var result = stockInId?.hashCode() ?: 0
            result = 31 * result + (stockInDate?.hashCode() ?: 0)
            result = 31 * result + (stockInTime?.hashCode() ?: 0)
            result = 31 * result + (stockInSupplierId?.hashCode() ?: 0)
            result = 31 * result + (totalProdPrice?.hashCode() ?: 0)
            return result
        }
    }