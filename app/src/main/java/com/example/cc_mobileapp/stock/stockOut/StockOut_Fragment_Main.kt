package com.example.cc_mobileapp.stock.stockOut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockOut
import com.example.cc_mobileapp.stock.stockDetail.StockDetailDisplayFragment
import com.example.cc_mobileapp.stock.stockOutDetail.StockOutDetailDisplayFragment
import kotlinx.android.synthetic.main.fragment_stock_out___main.*

class StockOut_Fragment_Main : Fragment(), StockOutRecyclerViewClickListener {
    // variable declaration
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
        // adapter and adapter listener set up on recycler view
        adapter.listener = this
        recycler_view_stockout.adapter = adapter
        // get data from database
        sharedStockOutViewModel.fetchStockOut()
        // get real time updates
        sharedStockOutViewModel.getRealtimeUpdates()

        // observe the changes in stocks out, if have changes, set the stock out information and make changes on UI
        sharedStockOutViewModel.stocksOut.observe(viewLifecycleOwner, Observer{
            adapter.setStocksOut(it)
        })
        // observe the changes in stock out, if have changes, set the stock out information and make changes on UI
        sharedStockOutViewModel.stockOut.observe(viewLifecycleOwner, Observer{
            adapter.addStockOut(it)
        })

        // button "+" is clicked, go to add fragment
        btn_stockOutDetail_add.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, StockOutClientFragment())
            transaction.addToBackStack("stockOutDetailFragment")
            transaction.commit()
        }
    }

    // when the "edit" button on the recycler view is click, go to edit stock out fragment with passing parameter of stock out
    override fun onRecyclerViewItemClicked(view: View, stockOut: StockOut) {
        when(view.id){
            R.id.btn_stockOutClientId-> {
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, StockOutDetailDisplayFragment(stockOut))
                transaction.addToBackStack("viewStockOutDetailRecycleView")
                transaction.commit()
            }
        }
    }

}