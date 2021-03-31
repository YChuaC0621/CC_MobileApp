package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude
import java.util.*

data class StockIn (
    @get:Exclude
    var stockInId: String? = null,
    var stockInDateTime: String? = null,
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
    }