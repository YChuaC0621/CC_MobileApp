package com.example.cc_mobileapp.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.fragment_product_report.*
import kotlinx.android.synthetic.main.fragment_stock_report.*
import java.time.LocalDate
import java.util.*

class StockReportFragment(private val startDate : String, private val endDate : String) : Fragment(), ProdReportRecycleViewClickListener {

    private lateinit var viewModel: ReportViewModel
    private val adapter = StockReportAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@StockReportFragment).get(ReportViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("Check", "stock report fragment")

        adapter.listener = this
        recycler_view_stockreport.adapter = adapter

        viewModel.fetchProdReportDetails()
        viewModel.fetchStockInReportDetails()
        viewModel.fetchStockOutReportDetails()
        viewModel.fetchStockInDetailReportDetails()
        viewModel.fetchStockOutDetailReportDetails()

        viewModel.reports.observe(viewLifecycleOwner, Observer {
            val prodList = it
            viewModel.stockin_reports.observe(viewLifecycleOwner, Observer {
                val stockInList = it
                viewModel.stockout_reports.observe(viewLifecycleOwner, Observer {
                    val stockOutList = it
                    viewModel.stockindetails_reports.observe(viewLifecycleOwner, Observer {
                        val stockInDetailList = it
                        viewModel.stockoutdetails_reports.observe(viewLifecycleOwner, Observer {
                            val stockOutDetailList = it
                            adapter.setStockReportDetails(prodList,startDate,endDate,stockInList,stockOutList,stockInDetailList,stockOutDetailList)
                            Log.d("Check", "stock report fragment on activity created$it")
                        })
                    })
                })
            })


        })

    }

    override fun onRecycleViewItemClicked(view: View, prodReport: Product) {
        TODO("Not yet implemented")
    }
}