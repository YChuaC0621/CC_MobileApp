package com.example.cc_mobileapp.stock.stockIn

import android.view.View
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn

interface StockInRecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, stockIn: StockIn)
}