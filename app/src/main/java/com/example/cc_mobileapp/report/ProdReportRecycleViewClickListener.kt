package com.example.cc_mobileapp.report

import android.view.View
import com.example.cc_mobileapp.model.Product

interface ProdReportRecycleViewClickListener {
    fun onRecycleViewItemClicked(view: View, prodReport: Product)
}