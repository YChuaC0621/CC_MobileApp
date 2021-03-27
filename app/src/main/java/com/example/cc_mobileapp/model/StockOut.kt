package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude
import java.util.*

data class StockOut (
    @get:Exclude
    var stockOutId: String? = null,
    var stockOutDateTime: String? = null,
    var stockOutClientId: String? = null
)
{
    override fun equals(other: Any?): Boolean
    {
        return if (other is StockOut)
        {
            other.stockOutId == stockOutId
        } else false
    }
}