package com.example.cc_mobileapp.stock.stockDetail

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
import com.example.cc_mobileapp.Constant.NODE_TEMP
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Rack
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_ProdBarcode
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_qty
import kotlinx.android.synthetic.main.fragment_add_stock_detail.edit_text_stockDetail_rackId
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.fragment_edit_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_stockout_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*
import kotlinx.coroutines.currentCoroutineContext

class EditStockDetailFragment(
        private val stockDetail: StockDetail
) : Fragment() {

    private lateinit var stockViewModel: StockViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private val dbProd = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)

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


        stockViewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.stockdetail_delete_success)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack("editStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        btn_editStockDetail_edit.setOnClickListener {
            val prodBarcode = edit_text_editStockDetail_ProdBarcode.text.toString().trim()
            val rackId = edit_text_editStockDetail_rackId.text.toString().trim()
            val stockQty: Int? = edit_text_editStockDetail_qty.text.toString().toIntOrNull()
            var valid: Boolean = true

            if (prodBarcode.isNullOrEmpty()) {
                input_layout_editStockDetail_ProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(prodBarcode)) {
                input_layout_editStockDetail_ProdBarcode.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_editStockDetail_ProdBarcode.error = null
            }

            if (rackId.isNullOrEmpty()) {
                input_layout_editStockDetail_rackId.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_editStockDetail_rackId.error = null
            }

            if (stockQty == null) {
                input_layout_editStockDetail_qty.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(stockQty.toString())) {
                input_layout_editStockDetail_qty.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            } else if (stockQty!! == 0) {
                input_layout_editStockDetail_qty.error = getString(R.string.stock_nonzero_error)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_editStockDetail_qty.error = null
            }

            if (valid) {
                val stockDetailEdit = StockDetail()
                stockDetailEdit.stockDetailProdBarcode = prodBarcode
                stockDetailEdit.stockDetailRackId = rackId
                stockDetailEdit.stockDetailQty = stockQty.toInt()
                stockDetailEdit.stockTypeId = sharedStockInViewModel.stockTypePushKey.value
                stockDetailEdit.stockDetailId = stockDetail.stockDetailId
                var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode.toString())
                prodBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            valid = false
                            input_layout_editStockDetail_ProdBarcode.error = getString(R.string.invalid_nonexist_error)
                        } else {
                            var checkExistProdBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(NODE_TEMP).orderByChild("stockDetailProdBarcode").equalTo(prodBarcode.toString())
                            checkExistProdBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        valid = false
                                        input_layout_editStockDetail_ProdBarcode.error = getString(R.string.exist_stockDetailProd_error)
                                    } else {
                                        var rackBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_RACK).orderByChild("rackName").equalTo(rackId.toString())
                                        rackBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                if (!snapshot.exists()) {
                                                    valid = false
                                                    input_layout_editStockDetail_rackId.error = getString(R.string.invalid_nonexist_error)
                                                } else {
                                                    var rackStatusQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_STOCKDETAIL)
                                                    rackStatusQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            var stockInUse: Boolean = false
                                                            for (stockSnapshot in snapshot.children) {
                                                                val occupiedRack = stockSnapshot.getValue(StockDetail::class.java)?.stockDetailRackId
                                                                if (occupiedRack == stockDetailEdit.stockDetailRackId) {
                                                                    stockInUse = true
                                                                }
                                                            }
                                                            if (stockInUse) {
                                                                input_layout_editStockDetail_rackId.error = getString(R.string.rack_occupied_error)
                                                            } else {
                                                                var tempRackQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_TEMP).orderByChild("stockDetailRackId").equalTo(rackId.toString())
                                                                tempRackQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                        if (snapshot.exists() && !stockDetailEdit.stockDetailRackId.equals(stockDetail.stockDetailRackId)) {
                                                                            valid = false
                                                                            input_layout_editStockDetail_rackId.error = getString(R.string.rack_occupiedontemp_error)
                                                                        } else {
                                                                            if (stockDetailEdit.stockDetailProdBarcode != stockDetail.stockDetailProdBarcode || stockDetailEdit.stockDetailQty != stockDetail.stockDetailQty || stockDetailEdit.stockDetailRackId != stockDetail.stockDetailRackId) {
                                                                                stockViewModel.updateStockDetail(stockDetailEdit)
                                                                                makeText(requireContext(), getString(R.string.stockdetail_success), Toast.LENGTH_SHORT).show()
                                                                            } else {
                                                                                makeText(requireContext(), getString(R.string.stockedit_info_remain), Toast.LENGTH_SHORT).show()
                                                                            }
                                                                            requireActivity().supportFragmentManager.popBackStack("editStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                                                        }
                                                                    }

                                                                    override fun onCancelled(error: DatabaseError) {
                                                                        TODO("Not yet implemented")
                                                                    }
                                                                })
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }
                                                    })
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }
                                        })
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
        btn_editStockDetail_cancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("editStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        btn_editStockDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
        }

        btn_editStockDetail_delete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which ->
                    stockViewModel.deleteStockDetail(stockDetail)
                    requireActivity().supportFragmentManager.popBackStack("editStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
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

        val rackListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                populateSearchRack(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbRack.addListenerForSingleValueEvent(rackListener)
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
            edit_text_editStockDetail_ProdBarcode.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
        }

    }

    protected fun populateSearchRack(snapshot: DataSnapshot) {
        var racks: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var currentRack: String = it.child("rackName").value.toString()
                racks.add(currentRack)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, racks)
            edit_text_editStockDetail_rackId.setAdapter(adapter)
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
        }
    }
}