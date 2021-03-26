package com.example.cc_mobileapp.stock.stockIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.stock.stockDetail.StockDetailFragment
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_in___main.*
import kotlinx.android.synthetic.main.fragment_stock_in_supplier_dialog.*

class StockInSupplierDialogFragment : Fragment() {

    private val sharedStockInViewModel: StockInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_in_supplier_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_continueAddStockInDetails.setOnClickListener {
            val stockInSupplierId:String = edit_text_stockIn_supplierId.text.toString().trim()
            when {
                stockInSupplierId.isEmpty() -> {
                    input_layout_stockIn_supplierId.error =
                        getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else ->{
                    sharedStockInViewModel.setStockInSupplierId(stockInSupplierId)
                }
            }
            val typePushKey: String = sharedStockInViewModel.generateTypePuskKey().toString()
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, StockDetailFragment())
            transaction.addToBackStack("stockInDetailFragment")
            transaction.commit()
        }

        btn_cancelAddStockInDetails.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("stockInSupplierDialogFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

}