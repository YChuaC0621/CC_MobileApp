package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude
import java.util.*

data class StockOut (
    @get:Exclude
    var stockOutId: String? = null,
    var stockOutDateTime: String? = null,
    var stockOutClientId: String? = null,
    var totalProdPrice: Double? = 0.00
)
{
    override fun equals(other: Any?): Boolean
    {
        return if (other is StockOut)
        {
            other.stockOutId == stockOutId
        } else false
    }

    override fun hashCode(): Int {
        var result = stockOutId?.hashCode() ?: 0
        result = 31 * result + (stockOutDateTime?.hashCode() ?: 0)
        result = 31 * result + (stockOutClientId?.hashCode() ?: 0)
        return result
    }
}