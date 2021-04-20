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
    // variable declaration
    private lateinit var stockInDetailViewModel: StockViewModel
    private lateinit var stockOutDetailViewModel: StockOutDetailViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockOutViewModel: StockOutViewModel by activityViewModels()
    private val dbProd = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private lateinit var prodViewModel: ProductViewModel
    private lateinit var barcodeListener:ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // provide this product with stock detail view model information
        stockInDetailViewModel =
            ViewModelProvider(this@AddStockOutDetailFragment).get(StockViewModel::class.java)
        stockOutDetailViewModel =
            ViewModelProvider(this@AddStockOutDetailFragment).get(StockOutDetailViewModel::class.java)
        prodViewModel =
            ViewModelProvider(this@AddStockOutDetailFragment).get(ProductViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_stockout_detail, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // observe if any changes on the view model result variable
        stockOutDetailViewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.prodAddSuccess)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        // if "+" button is clicked
        btn_stockOutDetail_add.setOnClickListener {
            // variable declaration and information from textbox is load
            var stockQty: Int? = null
            var prodBarcode: String = edit_text_stockOutDetail_ProdBarcode.text.toString().trim()
            stockQty = edit_text_stockOutDetail_qty.text.toString().toIntOrNull()
            var valid: Boolean = true

            // validate product barcode
            if (prodBarcode.isNullOrEmpty()) {
                input_layout_stockOutDetail_ProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(prodBarcode)) {
                input_layout_stockOutDetail_ProdBarcode.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_stockOutDetail_ProdBarcode.error = null
            }

            // validate stock quantity
            if (stockQty == null) {
                input_layout_stockOutDetail_qty.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexBarcode(stockQty.toString())) {
                input_layout_stockOutDetail_qty.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            } else if (stockQty!! == 0) {
                input_layout_stockOutDetail_qty.error = getString(R.string.stock_nonzero_error)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_stockOutDetail_qty.error = null
            }

            if (valid) {
                // construct new stock out detail with the information from input text
                val stockOutDetail = StockOutDetail()
                stockOutDetail.stockOutDetailProdBarcode = prodBarcode.trim()
                stockOutDetail.stockOutDetailQty = stockQty
                stockOutDetail.stockTypeId = sharedStockOutViewModel.stockOutTypePushKey.value
                var availableStockDB: Int = 0

                // validate on availability of the product barcode
                var checkExistProdBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(NODE_TEMP_OUT).orderByChild("stockOutDetailProdBarcode").equalTo(prodBarcode.toString())
                checkExistProdBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            valid = false
                            input_layout_stockOutDetail_ProdBarcode.error = getString(R.string.exist_stockDetailProd_error)
                        } else {
                            // check the availability of quantity of particular product
                            var prodBarcodeQuery: Query =
                                    FirebaseDatabase.getInstance().reference.child(NODE_PRODUCT)
                                            .orderByChild("prodBarcode").equalTo(prodBarcode.toString())
                            prodBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var availableStock: Boolean = false
                                    if (!snapshot.exists()) {
                                        valid = false
                                        input_layout_stockOutDetail_ProdBarcode.error = getString(R.string.invalid_nonexist_error)
                                    } else {
                                        for (checkProdSnapshot in snapshot.children) {
                                            var checkStockQtyProd =
                                                    checkProdSnapshot.getValue(Product::class.java)
                                            if (checkStockQtyProd?.prodQty!! >= stockOutDetail.stockOutDetailQty!!) {
                                                availableStock = true
                                                availableStockDB =
                                                        checkStockQtyProd.prodQty!! - stockOutDetail.stockOutDetailQty!!
                                            } else {
                                                valid = false
                                                input_layout_stockOutDetail_qty.error = getString(R.string.insufficientstock_error)
                                            }
                                        }
                                        if (availableStock) {
                                            //check the pending stock out product quantity
                                            var tempStockOutQuery: Query =
                                                    FirebaseDatabase.getInstance().reference.child(NODE_TEMP_OUT)
                                                            .orderByChild("stockOutDetailProdBarcode")
                                                            .equalTo(prodBarcode.toString())
                                            tempStockOutQuery.addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        for (tempStockOut in snapshot.children) {
                                                            var tempStockOutDB =
                                                                    tempStockOut.getValue(StockOutDetail::class.java)
                                                            if (tempStockOutDB?.stockOutDetailProdBarcode == stockOutDetail.stockOutDetailProdBarcode) {
                                                                if (availableStockDB >= tempStockOutDB?.stockOutDetailQty!!) {
                                                                    availableStockDB -= tempStockOutDB?.stockOutDetailQty!!
                                                                } else {
                                                                    valid = false
                                                                    input_layout_stockOutDetail_qty.error = getString(R.string.insufficientstock_error)
                                                                }
                                                            }
                                                        }
                                                        if (valid) {
                                                            stockOutDetailViewModel.addStockOutDetail(
                                                                    stockOutDetail
                                                            )
                                                            requireActivity().supportFragmentManager.popBackStack(
                                                                    "addStockOutDetailFragment",
                                                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                                            )
                                                        }
                                                    } else {
                                                        stockOutDetailViewModel.addStockOutDetail(stockOutDetail)
                                                        requireActivity().supportFragmentManager.popBackStack(
                                                                "addStockOutDetailFragment",
                                                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                                                        )
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

        // when "cancel" button is click, go back to previous fragment
        btn_stockOutDetail_cancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(
                "addStockOutDetailFragment",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )}

        // if scan barcode is click, go to scan barcode fragment
        btn_stockOutDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
        }

        // Autocomplete for product barcode
        barcodeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                populateSearchProdBarcode(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbProd.addListenerForSingleValueEvent(barcodeListener)
    }


    // vallidation on barcode
    private fun checkRegexBarcode(prodBarcode: String): Boolean {
        var prodBarcode: String = prodBarcode
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = prodBarcode)
    }


    // Autocomplete for product barcode
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

    // when returned from the scanning product, retrieve the information from the shared barcode view model to bind on the text box
    override fun onResume() {
        super.onResume()
        if(!sharedStockBarcodeViewModel.scannedProductCode.value.isNullOrEmpty()){
            edit_text_stockOutDetail_ProdBarcode.setText(sharedStockBarcodeViewModel.scannedProductCode.value)
            sharedStockBarcodeViewModel.clearBarcode()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbProd.removeEventListener(barcodeListener)
    }
}
