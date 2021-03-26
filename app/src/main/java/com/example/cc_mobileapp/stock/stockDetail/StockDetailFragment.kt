package com.example.cc_mobileapp.stock.stockDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.product.ScanBarcodeFragment
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_in___main.*

class StockDetailFragment : Fragment(), StockDetailRecyclerViewClickListener {

    private lateinit var viewModel: StockViewModel
    private val adapter = StockDetailAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(StockViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stock_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_stockDetail.adapter = adapter

        viewModel.getRealtimeUpdates()

        viewModel.stockDetail.observe(viewLifecycleOwner, Observer{
            adapter.addStockDetail(it)
        })

        btn_stockDetailAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment())
            transaction.addToBackStack("stockDetailFragment")
            transaction.commit()
        }
    }

    override fun onRecyclerViewItemClicked(view: View, stockDetail: StockDetail) {
        TODO("Not yet implemented")
    }

}