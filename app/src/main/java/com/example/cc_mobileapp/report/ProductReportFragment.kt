package com.example.cc_mobileapp.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.supplier.SupplierRecycleViewClickListener
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_product_report.*
import kotlinx.android.synthetic.main.fragment_supplier.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class ProductReportFragment: Fragment(), ProdReportRecycleViewClickListener {

    //data declaration
    private lateinit var viewModel: ReportViewModel
    private val adapter = ProductReportAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //bind view model to view
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@ProductReportFragment).get(ReportViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("Check", "product report fragment")

        adapter.listener = this
        recycler_view_prodreport.adapter = adapter

        //retrieve product details from database
        viewModel.fetchProdReportDetails()

        //retrieve the results
        viewModel.reports.observe(viewLifecycleOwner, Observer {
            //set value to each recycle view item
            adapter.setProdReportDetails(it)
            Log.d("Check", "product report fragment on activity created$it")
        })

    }

    override fun onRecycleViewItemClicked(view: View, prodReport: Product) {
        TODO("Not yet implemented")
    }

}