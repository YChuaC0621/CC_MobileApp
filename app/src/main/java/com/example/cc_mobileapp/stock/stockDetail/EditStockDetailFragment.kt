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
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_ProdBarcode
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_qty
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_rackId
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_rowNum
import kotlinx.android.synthetic.main.fragment_edit_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*

class EditStockDetailFragment(
        private val stockDetail: StockDetail
) : Fragment() {

    private lateinit var stockViewModel: StockViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        stockViewModel = ViewModelProvider(this@EditStockDetailFragment).get(StockViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_stock_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        edit_text_editStockDetail_ProdBarcode.setText(stockDetail.stockDetailProdBarcode)
        edit_text_editStockDetail_rackId.setText(stockDetail.stockDetailRackId)
        edit_text_editStockDetail_rowNum.setText(stockDetail.stockDetailRowNum)
        edit_text_editStockDetail_qty.setText(stockDetail.stockDetailQty)

        Log.e("Error", "check 1st stock detail$stockDetail")


        stockViewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it==null){
                getString(R.string.prodAddSuccess)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack("editStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        btn_editStockDetail_edit.setOnClickListener {
            val prodBarcode = edit_text_editStockDetail_ProdBarcode.text.toString().trim()
            val rackId = edit_text_editStockDetail_rackId.text.toString().trim()
            val rowNo = edit_text_editStockDetail_rowNum.text.toString().trim()
            val stockQty = edit_text_editStockDetail_qty.text.toString().trim()
            when {
                prodBarcode.isEmpty() -> {
                    input_layout_editStockDetail_ProdBarcode.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                rackId.isEmpty() -> {
                    input_layout_editStockDetail_rackId.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                rowNo.isEmpty() -> {
                    input_layout_editStockDetail_rowNum.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                stockQty.isEmpty() -> {
                    input_layout_editStockDetail_qty.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    Log.d("Check", "before val product")
                    val stockDetailEdit = StockDetail()
                    Log.d("Check", "after val product")
                    stockDetailEdit.stockDetailProdBarcode = prodBarcode
                    stockDetailEdit.stockDetailRackId = rackId
                    stockDetailEdit.stockDetailRowNum = rowNo
                    stockDetailEdit.stockDetailQty = stockQty
                    stockDetailEdit.stockTypeId = sharedStockInViewModel.stockTypePushKey.value
                    stockDetailEdit.stockDetailId = stockDetail.stockDetailId
                    Log.e("Error", stockDetailEdit.toString())

                    stockViewModel.updateStockDetail(stockDetailEdit)
                }
            }
        }

        btn_editStockDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
        }

        btn_editStockDetail_scanRackId.setOnClickListener {
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
            edit_text_editStockDetail_ProdBarcode.setText(sharedStockBarcodeViewModel.scannedProductCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
        else if(!sharedStockBarcodeViewModel.scannedRackCode.value.isNullOrEmpty()){
            edit_text_editStockDetail_rackId.setText(sharedStockBarcodeViewModel.scannedRackCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
        else{
            Toast.makeText(requireContext(), "viewModel have nothing", Toast.LENGTH_SHORT).show()
        }
    }
}