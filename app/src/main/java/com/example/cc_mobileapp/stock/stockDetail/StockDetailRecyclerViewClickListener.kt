package com.example.cc_mobileapp.stock.stockDetail

import android.view.View
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockDetail

interface StockDetailRecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, stockDetail: StockDetail)
}