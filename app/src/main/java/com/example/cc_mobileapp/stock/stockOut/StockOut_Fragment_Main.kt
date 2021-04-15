package com.example.cc_mobileapp.stock.stockOut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cc_mobileapp.R
import kotlinx.android.synthetic.main.fragment_stock_out___main.*

class StockOut_Fragment_Main : Fragment() {

    private val sharedStockOutViewModel: StockOutViewModel by activityViewModels()
    private val adapter = StockOutAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_out___main, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view_stockout.adapter = adapter

        sharedStockOutViewModel.fetchStockOut()
        sharedStockOutViewModel.getRealtimeUpdates()

        sharedStockOutViewModel.stocksOut.observe(viewLifecycleOwner, Observer{
            adapter.setStocksOut(it)
        })

        sharedStockOutViewModel.stockOut.observe(viewLifecycleOwner, Observer{
            adapter.addStockOut(it)
        })

        btn_stockOutDetail_add.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, StockOutClientFragment())
            transaction.addToBackStack("stockOutClientFragment")
            transaction.commit()
        }
    }

}