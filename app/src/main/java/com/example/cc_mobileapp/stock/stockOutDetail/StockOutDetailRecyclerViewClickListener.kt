package com.example.cc_mobileapp.stock.stockOutDetail

import android.view.View
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockOutDetail

interface StockOutDetailRecyclerViewClickListener {
    // interface to implement the on recycler view item clicked
    fun onRecyclerViewItemClicked(view: View, stockOutDetail: StockOutDetail)
}