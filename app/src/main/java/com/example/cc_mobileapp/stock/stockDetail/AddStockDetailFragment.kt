package com.example.cc_mobileapp.stock.stockDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.product.ScanBarcodeFragment
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.btn_addProdInDialog
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*

class AddStockDetailFragment : Fragment() {

    private lateinit var viewModel: StockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@AddStockDetailFragment).get(StockViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_stock_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it==null){
                getString(R.string.prodAddSuccess)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_stockDetailAdd.setOnClickListener {
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
                    // TODO add in the stockType Id
                    Log.d("Check", "client data $stockDetail")
                    viewModel.addStockDetail(stockDetail)
                    requireActivity().supportFragmentManager.popBackStack("stockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }

        btn_stockDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment())
            transaction.addToBackStack("addBarcodeFragment")
            transaction.commit()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if(!sharedBarcodeViewModel.scannedCode.value.isNullOrEmpty()){
//            edit_text_prodBarcode.setText(sharedBarcodeViewModel.scannedCode.value)
//            sharedBarcodeViewModel.clearBarcode()
//        }
//        else{
//            Toast.makeText(requireContext(), "viewModel have nothing", Toast.LENGTH_SHORT).show()
//        }
//    }

}