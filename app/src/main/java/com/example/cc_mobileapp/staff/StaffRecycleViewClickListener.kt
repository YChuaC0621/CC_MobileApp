package com.example.cc_mobileapp.staff

import android.view.View
import com.example.cc_mobileapp.model.Users
 interface StaffRecycleViewClickListener {
    open fun onRecycleViewItemClicked(view: View, user: Users){}
}