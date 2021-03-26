package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude
import java.util.*

// stock type id = id of stock in / stock out

data class StockDetail(
    @get:Exclude
    var stockDetailId: String? = null,
    var stockDetailProdBarcode: String? = null,
    var stockDetailRackId: String? = null,
    var stockDetailRowNum: String? = null,
    var stockDetailQty: String? = null,
    var stockTypeId: String? = null
)
{
    override fun equals(other: Any?): Boolean
    {
        return if (other is StockDetail)
        {
            other.stockDetailId == stockDetailId
        } else false
    }
}