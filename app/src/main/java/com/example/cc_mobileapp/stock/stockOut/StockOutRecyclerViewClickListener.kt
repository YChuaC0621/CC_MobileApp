package com.example.cc_mobileapp.stock.stockOut

import android.view.View
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.model.StockOut

interface StockOutRecyclerViewClickListener {
    // interface to implement the on recycler view item clicked
    fun onRecyclerViewItemClicked(view: View, stockOut: StockOut)
}