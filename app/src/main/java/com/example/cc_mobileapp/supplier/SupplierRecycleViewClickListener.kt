package com.example.cc_mobileapp.supplier

import android.view.View
import com.example.cc_mobileapp.model.Supplier

interface SupplierRecycleViewClickListener {
    fun onRecycleViewItemClicked(view: View, supplier: Supplier)
}
