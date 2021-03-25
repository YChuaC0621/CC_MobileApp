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
    var prodPrice: Float? = null
)