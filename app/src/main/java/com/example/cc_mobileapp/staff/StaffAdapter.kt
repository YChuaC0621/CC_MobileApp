package com.example.cc_mobileapp.staff

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Users
import kotlinx.android.synthetic.main.staff_display_item.view.*

class StaffAdapter: RecyclerView.Adapter<StaffAdapter.StaffViewModel>(){

    private var staff = mutableListOf<Users>()
    var listener: StaffRecycleViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = StaffViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.staff_display_item, parent, false)
    )

    override fun getItemCount() = staff.size

    override fun onBindViewHolder(holder: StaffViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtStaffName.text = staff[position].userName
        holder.view.txtStaffEmail.text = staff[position].userEmail
        holder.view.btn_staffEdit.setOnClickListener { listener?.onRecycleViewItemClicked(it, staff[position])}
    }

    fun setStaffs(staffs: List<Users>){
        Log.d("Check", "staff set: $staffs")
        this.staff = staffs as MutableList<Users>
        notifyDataSetChanged()
    }


    class StaffViewModel(val view: View) : RecyclerView.ViewHolder(view)
}