package com.example.cc_mobileapp.rack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.rack.RackViewModel
import com.example.cc_mobileapp.supplier.AddSupplierDialogFragment
import com.example.cc_mobileapp.supplier.SupplierViewModel
import kotlinx.android.synthetic.main.fragment_sitemap.*
import kotlinx.android.synthetic.main.fragment_supplier.*
import kotlinx.android.synthetic.main.fragment_supplier.button_add

class SiteMapFragment: Fragment() {
    private lateinit var viewModel: RackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@SiteMapFragment).get(RackViewModel::class.java)

        return inflater.inflate(R.layout.fragment_sitemap, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Check", "supplier fragment on activity created")

        super.onActivityCreated(savedInstanceState)


        button_add.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddRackDialogFragment())
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack1.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack1.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack2.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack2.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack3.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack3.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack4.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack4.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack20.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack20.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

    }
}