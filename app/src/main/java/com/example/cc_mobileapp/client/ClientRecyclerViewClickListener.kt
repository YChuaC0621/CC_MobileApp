package com.example.cc_mobileapp.client

import android.view.View
import com.example.cc_mobileapp.model.Client

interface ClientRecyclerViewClickListener {
    // interface to implement the on recycler view item clicked
    fun onRecycleViewItemClicked(view: View, client: Client)
}