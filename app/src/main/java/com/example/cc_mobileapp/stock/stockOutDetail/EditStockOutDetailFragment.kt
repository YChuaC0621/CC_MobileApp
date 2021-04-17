package com.example.cc_mobileapp.stock.stockOutDetail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.Rack
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.StockOutDetail
import com.example.cc_mobileapp.stock.stockDetail.StockBarcodeViewModel
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_ProdBarcode
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_qty
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_rackId
import kotlinx.android.synthetic.main.fragment_add_stockout_detail.*
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.fragment_edit_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_stockout_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.android.synthetic.main.stockoutdetail_display_item.*
import kotlinx.coroutines.currentCoroutineContext

class EditStockOutDetailFragment(
        private val stockOutDetail: StockOutDetail
) : Fragment() {

    private lateinit var stockOutDetailViewModel: StockOutDetailViewModel
    private val sharedStockBarcodeViewModel: StockOutBarcodeViewModel by activityViewModels()
    private val sharedStockOutDetailViewModel: StockOutDetailViewModel by activityViewModels()
    private val dbProd = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        stockOutDetailViewModel = ViewModelProvider(this@EditStockOutDetailFragment).get(StockOutDetailViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_stockout_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edit_text_editStockOutDetail_ProdBarcode.setText(stockOutDetail.stockOutDetailProdBarcode.toString().trim())
        edit_text_editStockOutDetail_qty.setText(stockOutDetail.stockOutDetailQty.toString())

        Log.e("Error", "check 1st stock out detail$stockOutDetail")


        stockOutDetailViewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.stockdetail_delete_success)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack("editStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        btn_editStockOutDetail_edit.setOnClickListener {
            val prodBarcode = edit_text_editStockOutDetail_ProdBarcode.text.toString().trim()
            val stockQty: Int? = edit_text_editStockOutDetail_qty.text.toString().toIntOrNull()
            var valid: Boolean = true

            if (prodBarcode.isNullOrEmpty()) {
                input_layout_editStockOutDetail_ProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(prodBarcode)) {
                input_layout_editStockOutDetail_ProdBarcode.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_editStockOutDetail_ProdBarcode.error = null
            }

            if (stockQty == null) {
                input_layout_editStockOutDetail_qty.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(stockQty.toString())) {
                input_layout_editStockOutDetail_qty.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            } else if (stockQty!! == 0) {
                input_layout_editStockOutDetail_qty.error = getString(R.string.stock_nonzero_error)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_editStockOutDetail_qty.error = null
            }

            if (valid) {
                val stockOutDetailEdit = StockOutDetail()
                stockOutDetailEdit.stockOutDetailProdBarcode = prodBarcode
                stockOutDetailEdit.stockOutDetailQty = stockQty
                stockOutDetailEdit.stockTypeId = sharedStockOutDetailViewModel.stockOutTypeKey.value
                stockOutDetailEdit.stockOutDetailId = stockOutDetail.stockOutDetailId
                var availableStockDB: Int = 0

                if (stockOutDetailEdit.stockOutDetailProdBarcode != stockOutDetail.stockOutDetailProdBarcode || stockOutDetailEdit.stockOutDetailQty != stockOutDetail.stockOutDetailQty)
                {
                    var checkExistProdBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_TEMP_OUT).orderByChild("stockOutDetailProdBarcode").equalTo(prodBarcode.toString()).also {
                        it.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    valid = false
                                    input_layout_editStockOutDetail_ProdBarcode.error = getString(R.string.exist_stockDetailProd_error)
                                } else {
                                    var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode.toString())
                                    prodBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var availableStock: Boolean = false
                                            if (!snapshot.exists()) {
                                                valid = false
                                                input_layout_editStockOutDetail_ProdBarcode.error = getString(R.string.invalid_nonexist_error)
                                            } else {
                                                for (checkProdSnapshot in snapshot.children) {
                                                    var checkStockQtyProd = checkProdSnapshot.getValue(Product::class.java)
                                                    if (checkStockQtyProd?.prodQty!! >= stockOutDetailEdit.stockOutDetailQty!!) {
                                                        availableStock = true
                                                        availableStockDB = checkStockQtyProd.prodQty!! - stockOutDetailEdit.stockOutDetailQty!!
                                                    } else {
                                                        valid = false
                                                        input_layout_editStockOutDetail_qty.error = getString(R.string.insufficientstock_error)
                                                    }
                                                }
                                                if (availableStock) {
                                                    var tempStockOutQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_TEMP_OUT).orderByChild("stockOutDetailProdBarcode").equalTo(prodBarcode.toString())
                                                    tempStockOutQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            if (snapshot.exists()) {
                                                                for (tempStockOut in snapshot.children) {
                                                                    var tempStockOutDB = tempStockOut.getValue(StockOutDetail::class.java)
                                                                    tempStockOutDB?.stockOutDetailId = tempStockOut.key
                                                                    if (stockOutDetailEdit.stockOutDetailId != tempStockOutDB?.stockOutDetailId) {
                                                                        if (tempStockOutDB?.stockOutDetailProdBarcode == stockOutDetailEdit.stockOutDetailProdBarcode) {
                                                                            if (availableStockDB >= tempStockOutDB?.stockOutDetailQty!!) {
                                                                                availableStockDB -= tempStockOutDB?.stockOutDetailQty!!
                                                                            } else {
                                                                                valid = false
                                                                                input_layout_editStockOutDetail_qty.error = getString(R.string.insufficientstock_error)
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (valid) {
                                                                    stockOutDetailViewModel.updateStockOutDetail(stockOutDetailEdit)
                                                                    Toast.makeText(requireActivity(), getString(R.string.stockdetail_success), Toast.LENGTH_SHORT).show()
                                                                    requireActivity().supportFragmentManager.popBackStack("editStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                                                }
                                                            } else {
                                                                stockOutDetailViewModel.updateStockOutDetail(stockOutDetailEdit)
                                                                Toast.makeText(requireActivity(), getString(R.string.stockdetail_success), Toast.LENGTH_SHORT).show()
                                                                requireActivity().supportFragmentManager.popBackStack("editStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }
                                                    })
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                        // FIFO check stock


                                        // TODO scare of duplication of same stock in transac -> get limit to one
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }
            else
            {
                Toast.makeText(requireActivity(),getString(R.string.stockedit_info_remain), Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack("editStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

                btn_editStockOutDetail_cancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("editStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        btn_editStockOutDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragmentStockOut("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
        }

        btn_editStockOutDetail_delete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also {
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    stockOutDetailViewModel.deleteStockOutDetail(stockOutDetail)
                }
                it.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            }.create().show()
        }

        // Autocomplete for product barcode
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

    private fun checkRegexBarcode(prodBarcode: String): Boolean {
        var prodBarcode: String = prodBarcode
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = prodBarcode)
    }

    protected fun populateSearchProdBarcode(snapshot: DataSnapshot) {
        var prodBarcodes: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var barcodeStored: String = it.child("prodBarcode").value.toString()
                prodBarcodes.add(barcodeStored)
            }
            var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, prodBarcodes)
            edit_text_editStockOutDetail_ProdBarcode.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
        }
    }

    override fun onResume() {
        super.onResume()
        if(!sharedStockBarcodeViewModel.scannedProductCode.value.isNullOrEmpty()){
            edit_text_editStockOutDetail_ProdBarcode.setText(sharedStockBarcodeViewModel.scannedProductCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
    }
}