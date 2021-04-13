package com.example.cc_mobileapp.stock.stockDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_ProdBarcode
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_qty
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_rackId
import kotlinx.android.synthetic.main.fragment_edit_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*

class EditStockDetailFragment(
        private val stockDetail: StockDetail
) : Fragment() {

    private lateinit var stockViewModel: StockViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private val dbProd = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)

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


        edit_text_editStockDetail_ProdBarcode.setText(stockDetail.stockDetailProdBarcode.toString())
        edit_text_editStockDetail_rackId.setText(stockDetail.stockDetailRackId)
        edit_text_editStockDetail_qty.setText(stockDetail.stockDetailQty.toString())

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
                    stockDetailEdit.stockDetailProdBarcode = prodBarcode.toInt()
                    stockDetailEdit.stockDetailRackId = rackId
                    stockDetailEdit.stockDetailQty = stockQty.toInt()
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

        // Validation for product barcode
        val barcodeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                populateSearchProdBarcode(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbProd.addListenerForSingleValueEvent(barcodeListener)
    }

    protected fun populateSearchProdBarcode(snapshot: DataSnapshot) {
        var prodBarcodes: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var barcodeStored: String = it.child("prodBarcode").value.toString()
                prodBarcodes.add(barcodeStored)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, prodBarcodes)
            edit_text_editStockDetail_ProdBarcode.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
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