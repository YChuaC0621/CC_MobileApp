package com.example.cc_mobileapp.stock.stockIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.stock.stockDetail.EditStockDetailFragment
import com.example.cc_mobileapp.stock.stockDetail.StockDetailDisplayFragment
import kotlinx.android.synthetic.main.fragment_stock_in___main.*

class StockIn_Fragment_Main : Fragment(), StockInRecyclerViewClickListener  {

    // variable declaration
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private val adapter = StockInAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_in___main, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // adapter and adapter listener set up on recycler view
        adapter.listener = this
        recycler_view_stockin.adapter = adapter

        // get data from database
        sharedStockInViewModel.fetchStockIn()
        // get real time updates
        sharedStockInViewModel.getRealtimeUpdates()

        // observe the changes in stocks in, if have changes, set the stock in information and make changes on UI
        sharedStockInViewModel.stocksIn.observe(viewLifecycleOwner, Observer{
            adapter.setStocksIn(it)
        })

        // observe the changes in stock in, if have changes, set the stock in information and make changes on UI
        sharedStockInViewModel.stockIn.observe(viewLifecycleOwner, Observer{
            adapter.addStockIn(it)
        })

        // button "+" is clicked, go to add fragment
        btn_stockInDetail_add.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, StockInSupplierDialogFragment())
            transaction.addToBackStack("stockInSupplierDialogFragment")
            transaction.commit()
        }
    }

    // when the "edit" button on the recycler view is click, go to edit stock in fragment with passing parameter of stock in
    override fun onRecyclerViewItemClicked(view: View, stockIn: StockIn) {
        when(view.id){
            R.id.btn_stockInSupplierId-> {
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, StockDetailDisplayFragment(stockIn))
                transaction.addToBackStack("viewStockInDetailRecycleView")
                transaction.commit()
            }
        }
    }

}