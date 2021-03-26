package com.example.cc_mobileapp.stock.stockIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.ClientRecyclerViewClickListener
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.product.*
import com.example.cc_mobileapp.stock.stockDetail.StockDetailFragment
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_stock_in___main.*

class StockIn_Fragment_Main : Fragment() {

    private lateinit var viewModel: StockInViewModel
    private val adapter = StockInAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(StockInViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stock_in___main, container, false)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view_stockin.adapter = adapter

        viewModel.fetchStockIn()
        viewModel.getRealtimeUpdates()

        viewModel.stocksIn.observe(viewLifecycleOwner, Observer{
            adapter.setStocksIn(it)
        })

        viewModel.stockIn.observe(viewLifecycleOwner, Observer{
            adapter.addStockIn(it)
        })

        btn_stockInDetail_add.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, StockDetailFragment())
            transaction.addToBackStack("stockDetailFragment")
            transaction.commit()
        }
    }

}