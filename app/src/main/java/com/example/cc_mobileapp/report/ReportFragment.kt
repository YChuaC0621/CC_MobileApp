package com.example.cc_mobileapp.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.rack.AddRackDialogFragment
import com.example.cc_mobileapp.rack.RackViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_supplier.*

class ReportFragment: Fragment() {
    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@ReportFragment).get(ReportViewModel::class.java)

        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Check", "report fragment on activity created")

        super.onActivityCreated(savedInstanceState)


        btn_product.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ProductReportFragment())
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_stocks.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, StockReportFormFragment())
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }
    }
}