package com.example.cc_mobileapp.productReport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.supplier.*
import com.example.cc_mobileapp.model.Supplier
import kotlinx.android.synthetic.main.fragment_supplier.*

class ReportFragment: Fragment(), SupplierRecycleViewClickListener {

    private lateinit var viewModel: SupplierViewModel
    private val adapter = SupplierAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@ReportFragment).get(SupplierViewModel::class.java)

        return inflater.inflate(R.layout.fragment_supplier, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Check", "supplier fragment on activity created")

        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_supplier.adapter = adapter

        viewModel.fetchSuppliers()
        viewModel.getRealtimeUpdates()

        viewModel.suppliers.observe(viewLifecycleOwner, Observer {
            adapter.setSuppliers(it)
            Log.d("Check", "supplier fragment on activity created$it")
        })

        viewModel.supplier.observe(viewLifecycleOwner, Observer {
            Log.d("Check", "B4 realtime add  supplier fragment on activity created$it")
            adapter.addSupplier(it)
            Log.d("Check", "realtime add  supplier fragment on activity created$it")
        })

        button_add.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddSupplierDialogFragment())
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }


    }

    override fun onRecycleViewItemClicked(view: View, supplier: Supplier) {
        val currentView = (requireView().parent as ViewGroup).id
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(currentView, EditSupplierFragment(supplier))
        transaction.addToBackStack("fragmentA")
        transaction.commit()
    }
}