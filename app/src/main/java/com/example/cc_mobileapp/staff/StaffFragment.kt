package com.example.cc_mobileapp.staff

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Users
import kotlinx.android.synthetic.main.fragment_staff.*

class StaffFragment : Fragment(), StaffRecycleViewClickListener{

    //data declaration
    private lateinit var viewModel: StaffViewModel
    private val adapter = StaffAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //bind view model to view
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@StaffFragment).get(StaffViewModel::class.java)

        return inflater.inflate(R.layout.fragment_staff, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_staff.adapter = adapter

        //retrieve staff information and keep update if any changes
        viewModel.fetchStaff()
        viewModel.getRealtimeUpdates()

        //observe the results and set to each recycle view item
        viewModel.staffs.observe(viewLifecycleOwner, Observer {
            adapter.setStaffs(it)
            Log.d("Check", "staffs fragment on activity created$it")
        })

        viewModel.staff.observe(viewLifecycleOwner, Observer {
            Log.d("Check", "B4 realtime add  supplier fragment on activity created$it")
            adapter.addStaff(it)
            Log.d("Check", "realtime add  supplier fragment on activity created$it")
        })



    }

    override fun onRecycleViewItemClicked(view: View, staff: Users) {
        val currentView = (requireView().parent as ViewGroup).id
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(currentView, EditStaffFragment(staff))
        transaction.addToBackStack("fragmentA")
        transaction.commit()
    }
}