package com.example.cc_mobileapp.stock.stockDetail

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AlertDialogLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockIn
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import java.util.*

class StockDetailFragment : Fragment(), StockDetailRecyclerViewClickListener {

    private val adapter = StockDetailAdapter()
    private lateinit var stockViewModel: StockViewModel
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        stockViewModel = ViewModelProvider(this@StockDetailFragment).get(StockViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stock_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_stockDetail.adapter = adapter

        stockViewModel.fetchStockDetail()

        stockViewModel.getRealtimeUpdates()

        stockViewModel.stocksDetail.observe(viewLifecycleOwner, Observer{
            adapter.setStocksDetail(it)
        })

        stockViewModel.stockDetail.observe(viewLifecycleOwner, Observer{
            adapter.addStockDetail(it)
        })

        btn_stockDetailAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddStockDetailFragment())
            transaction.addToBackStack("addStockDetailFragment")
            transaction.commit()
        }

        btn_stockDetailSave.setOnClickListener {
            var totalPrice = stockViewModel.storedIntoStockInDB()
            val stockIn = StockIn()
            stockIn.stockInDateTime = Calendar.getInstance().time.toString()
            stockIn.stockInSupplierId = sharedStockInViewModel.stockInSupplierId.value
            stockIn.totalProdPrice = totalPrice
            sharedStockInViewModel.addStockIn(stockIn)
            viewModelStore.clear()
            requireActivity().supportFragmentManager.popBackStack(getCallerFragment(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun getCallerFragment(): String? {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
        Toast.makeText(requireContext(), fm.getBackStackEntryAt(count - 2).name, Toast.LENGTH_SHORT).show()
        return fm.getBackStackEntryAt(count - 2).name
    }

    override fun onRecyclerViewItemClicked(view: View, stockDetail: StockDetail) {
        when(view.id){
            R.id.btn_edit_stockDetail -> {
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, EditStockDetailFragment(stockDetail))
                transaction.addToBackStack("editStockDetailFragment")
                transaction.commit()
            }
            R.id.btn_delete_stockDetail ->{
                AlertDialog.Builder(requireContext()).also{
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)){ dialog, which ->
                        stockViewModel.deleteStockDetail(stockDetail)
                    }
                }.create().show()
            }
        }
    }
}

