package com.example.cc_mobileapp.product

import android.view.View
import com.example.cc_mobileapp.model.Product

interface ProductRecyclerViewClickListener {
    // interface to implement the on recycler view item clicked
    fun onRecyclerViewItemClicked(view: View, product: Product)
}