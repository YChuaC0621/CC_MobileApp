package com.example.cc_mobileapp.stock.stockDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*

class AddStockDetailFragment : Fragment() {

    private lateinit var stockViewModel: StockViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        stockViewModel = ViewModelProvider(this@AddStockDetailFragment).get(StockViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_stock_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.e("StockIN", "access add stock detail fragment on activity created ")

        stockViewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it==null){
                getString(R.string.prodAddSuccess)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_stockDetail_add.setOnClickListener {
            val prodBarcode = edit_text_stockDetail_ProdBarcode.text.toString().trim()
            val rackId = edit_text_stockDetail_rackId.text.toString().trim()
            val rowNo = edit_text_stockDetail_rowNum.text.toString().trim()
            val stockQty = edit_text_stockDetail_qty.text.toString().trim()
            when {
                prodBarcode.isEmpty() -> {
                    input_layout_prodName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                rackId.isEmpty() -> {
                    input_layout_prodSupplierId.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                rowNo.isEmpty() -> {
                    input_layout_prodDesc.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                stockQty.isEmpty() -> {
                    input_layout_prodPrice.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    Log.d("Check", "before val product")
                    val stockDetail = StockDetail()
                    Log.d("Check", "after val product")
                    stockDetail.stockDetailProdBarcode = prodBarcode
                    stockDetail.stockDetailRackId = rackId
                    stockDetail.stockDetailRowNum = rowNo
                    stockDetail.stockDetailQty = stockQty
                    stockDetail.stockTypeId = sharedStockInViewModel.stockTypePushKey.value
                    // TODO add in the stockType Id
                    Log.d("Check", "client data $stockDetail")
                    stockViewModel.addStockDetail(stockDetail)
                    requireActivity().supportFragmentManager.popBackStack("addStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }
        }

        btn_stockDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
        }

        btn_stockDetail_scanRackId.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("rack"))
            transaction.addToBackStack("rackBarcode")
            transaction.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!sharedStockBarcodeViewModel.scannedProductCode.value.isNullOrEmpty()){
            edit_text_stockDetail_ProdBarcode.setText(sharedStockBarcodeViewModel.scannedProductCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
        else if(!sharedStockBarcodeViewModel.scannedRackCode.value.isNullOrEmpty()){
            edit_text_stockDetail_rackId.setText(sharedStockBarcodeViewModel.scannedRackCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
        else{
            Toast.makeText(requireContext(), "viewModel have nothing", Toast.LENGTH_SHORT).show()
        }
    }
}