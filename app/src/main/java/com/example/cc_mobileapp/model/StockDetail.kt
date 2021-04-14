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
    var stockDetailQty: Int? = null,
    var stockTypeId: String? = null,
    var stockStatus: Boolean? = false,
    @get:Exclude
    var isDeleted: Boolean = false
)
{
    override fun equals(other: Any?): Boolean
    {
        return if (other is StockDetail)
        {
            other.stockDetailId == stockDetailId
        } else false
    }

    override fun hashCode(): Int {
        var result = stockDetailId?.hashCode() ?: 0
        result = 31 * result + (stockDetailProdBarcode?.hashCode() ?: 0)
        result = 31 * result + (stockDetailRackId?.hashCode() ?: 0)
        result = 31 * result + (stockDetailQty ?: 0)
        result = 31 * result + (stockTypeId?.hashCode() ?: 0)
        result = 31 * result + (stockStatus?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }
}