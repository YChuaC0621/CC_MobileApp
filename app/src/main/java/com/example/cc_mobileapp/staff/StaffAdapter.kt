package com.example.cc_mobileapp.staff

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Supplier
import com.example.cc_mobileapp.model.Users
import kotlinx.android.synthetic.main.staff_display_item.view.*

class StaffAdapter: RecyclerView.Adapter<StaffAdapter.StaffViewModel>(){

    //data declaration
    private var staff = mutableListOf<Users>()
    var listener: StaffRecycleViewClickListener? = null

    //bind view model to each recycle view
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = StaffViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.staff_display_item, parent, false)
    )

    override fun getItemCount() = staff.size

    //set value to each recycle view item
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

    //add to database
    fun addStaff(staff: Users) {
        Log.d("Check", "real time add supplier $staff")
        if (!this.staff.contains(staff)) {
            this.staff.add(staff)
        }else{  // delete supplier
            val staffUpdateIndex = this.staff.indexOf(staff)
            if(staff.workingStatus == 0){
                this.staff.removeAt(staffUpdateIndex)
            }else{ // for update client
                this.staff[staffUpdateIndex] = staff
            }
        }
        notifyDataSetChanged()
    }


    class StaffViewModel(val view: View) : RecyclerView.ViewHolder(view)
}