package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude
import java.util.*

// stock type id = id of stock in / stock out

data class StockOutDetail(
    @get:Exclude
    var stockOutDetailId: String? = null,
    var stockOutDetailProdBarcode: String? = null,
    var stockOutDetailQty: Int? = null,
    var stockTypeId: String? = null,
    @get:Exclude
    var isDeleted: Boolean = false
)
{
    override fun equals(other: Any?): Boolean
    {
        return if (other is StockOutDetail)
        {
            other.stockOutDetailId == stockOutDetailId
        } else false
    }

    override fun hashCode(): Int {
        var result = stockOutDetailId?.hashCode() ?: 0
        result = 31 * result + (stockOutDetailProdBarcode?.hashCode() ?: 0)
        result = 31 * result + (stockOutDetailQty ?: 0)
        result = 31 * result + (stockTypeId?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }
}