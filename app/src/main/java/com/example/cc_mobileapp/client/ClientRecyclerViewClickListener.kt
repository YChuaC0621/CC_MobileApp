package com.example.cc_mobileapp.client

import android.view.View
import com.example.cc_mobileapp.model.Client

interface ClientRecyclerViewClickListener {
    fun onRecycleViewItemClicked(view: View, client: Client)
}