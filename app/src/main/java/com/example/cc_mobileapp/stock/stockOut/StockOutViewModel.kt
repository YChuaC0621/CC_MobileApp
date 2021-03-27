package com.example.cc_mobileapp.stock.stockOut

import com.example.cc_mobileapp.Constant
import com.google.firebase.database.FirebaseDatabase

class StockOutViewModel {
    private val dbStockOut = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)

}