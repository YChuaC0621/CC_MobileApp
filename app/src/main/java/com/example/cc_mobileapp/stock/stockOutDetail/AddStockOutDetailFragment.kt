package com.example.cc_mobileapp.stock.stockDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.Constant.NODE_PRODUCT
import com.example.cc_mobileapp.Constant.NODE_STOCKDETAIL
import com.example.cc_mobileapp.Constant.NODE_STOCKIN
import com.example.cc_mobileapp.Constant.NODE_STOCKOUT
import com.example.cc_mobileapp.Constant.NODE_TEMP
import com.example.cc_mobileapp.Constant.NODE_TEMP_OUT
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockOutDetail
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.stock.stockOut.StockOutViewModel
import com.example.cc_mobileapp.stock.stockOutDetail.StockOutDetailViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_add_stockout_detail.*
import kotlinx.android.synthetic.main.fragment_edit_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*


class AddStockOutDetailFragment : Fragment() {

    private lateinit var stockInDetailViewModel: StockViewModel
    private lateinit var stockOutDetailViewModel: StockOutDetailViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockOutViewModel: StockOutViewModel by activityViewModels()
    private val dbProd = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)
    private lateinit var prodViewModel: ProductViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        stockInDetailViewModel = ViewModelProvider(this@AddStockOutDetailFragment).get(StockViewModel::class.java)
        stockOutDetailViewModel = ViewModelProvider(this@AddStockOutDetailFragment).get(StockOutDetailViewModel::class.java)
        prodViewModel = ViewModelProvider(this@AddStockOutDetailFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_stockout_detail, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stockOutDetailViewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.prodAddSuccess)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_stockOutDetail_add.setOnClickListener {
            var stockQty: Int? = null
            var prodBarcode: String = edit_text_stockOutDetail_ProdBarcode.text.toString()
            stockQty = edit_text_stockOutDetail_qty.text.toString().toIntOrNull()
            var valid: Boolean = true

            if (prodBarcode.isNullOrEmpty()) {
                input_layout_stockOutDetail_ProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(prodBarcode)) {
                input_layout_stockOutDetail_ProdBarcode.error = "Only integer are allowed"
                valid = false
                return@setOnClickListener
            } else {
                input_layout_stockOutDetail_ProdBarcode.error = null
            }

            if (stockQty == null) {
                input_layout_stockOutDetail_qty.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(stockQty.toString())) {
                input_layout_stockOutDetail_qty.error = "Invalid quantity input"
                valid = false
                return@setOnClickListener
            } else if (stockQty!! == 0) {
                input_layout_stockOutDetail_qty.error = "Product Quantity cannot be zero"
                valid = false
                return@setOnClickListener
            } else {
                input_layout_stockOutDetail_qty.error = null
            }

            if (valid) {
                val stockOutDetail = StockOutDetail()
                stockOutDetail.stockOutDetailProdBarcode = prodBarcode
                stockOutDetail.stockOutDetailQty = stockQty
                stockOutDetail.stockTypeId = sharedStockOutViewModel.stockOutTypePushKey.value
                var availableStockDB: Int = 0
                var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode.toString())
                prodBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var availableStock: Boolean = false
                        if (!snapshot.exists()) {
                            valid = false
                            input_layout_stockOutDetail_ProdBarcode.error = "Invalid product barcode"
                        } else {
                            for (checkProdSnapshot in snapshot.children) {
                                var checkStockQtyProd = checkProdSnapshot.getValue(Product::class.java)
                                if (checkStockQtyProd?.prodQty!! >= stockOutDetail.stockOutDetailQty!!) {
                                    availableStock = true
                                    availableStockDB = checkStockQtyProd.prodQty!! - stockOutDetail.stockOutDetailQty!!
                                }
                                else{
                                    valid = false
                                    input_layout_stockOutDetail_qty.error = "Insufficient stock quantity"
                                }
                            }
                            if (availableStock) {
                                var tempStockOutQuery: Query = FirebaseDatabase.getInstance().reference.child(NODE_TEMP_OUT).orderByChild("stockOutDetailProdBarcode").equalTo(prodBarcode.toString())
                                tempStockOutQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            for (tempStockOut in snapshot.children){
                                                var tempStockOutDB = tempStockOut.getValue(StockOutDetail::class.java)
                                                if(tempStockOutDB?.stockOutDetailProdBarcode == stockOutDetail.stockOutDetailProdBarcode){
                                                    if(availableStockDB >= tempStockOutDB?.stockOutDetailQty!!){
                                                        availableStockDB -= tempStockOutDB?.stockOutDetailQty!!
                                                    }else{
                                                        valid = false
                                                        input_layout_stockOutDetail_qty.error = "Insufficient stock quantity"
                                                    }
                                                }
                                            }
                                            if(valid){
                                            stockOutDetailViewModel.addStockOutDetail(stockOutDetail)
                                            requireActivity().supportFragmentManager.popBackStack("addStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)                                            }
                                        } else {
                                            stockOutDetailViewModel.addStockOutDetail(stockOutDetail)
                                            requireActivity().supportFragmentManager.popBackStack("addStockOutDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

        btn_stockOutDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
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
        if (snapshot.exists()) {
            snapshot.children.forEach {
                var prodBarcode: String = it.child("prodBarcode").value.toString()
                prodBarcodes.add(prodBarcode)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, prodBarcodes)
            edit_text_stockOutDetail_ProdBarcode.setAdapter(adapter)
        } else {
            Log.d("checkAuto", "No match found")
        }
    }

    override fun onResume() {
        super.onResume()
        if(!sharedStockBarcodeViewModel.scannedProductCode.value.isNullOrEmpty()){
            edit_text_stockOutDetail_ProdBarcode.setText(sharedStockBarcodeViewModel.scannedProductCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
    }
}
