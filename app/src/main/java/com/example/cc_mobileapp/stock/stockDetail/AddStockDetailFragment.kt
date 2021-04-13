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
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.product.ProductViewModel
import com.example.cc_mobileapp.stock.stockIn.StockInViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_detail.*


class AddStockDetailFragment : Fragment() {

    private lateinit var stockViewModel: StockViewModel
    private val sharedStockBarcodeViewModel: StockBarcodeViewModel by activityViewModels()
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    private val dbProd = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)
    private lateinit var prodViewModel: ProductViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        stockViewModel = ViewModelProvider(this@AddStockDetailFragment).get(StockViewModel::class.java)
        prodViewModel = ViewModelProvider(this@AddStockDetailFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_stock_detail, container, false)
    }

    var prodBarcode: Int? = null
    var stockQty: Int? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stockViewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.prodAddSuccess)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_stockDetail_add.setOnClickListener {
            prodBarcode = edit_text_stockDetail_ProdBarcode.text.toString().toIntOrNull()
            val rackId = edit_text_stockDetail_rackId.text.toString().trim()
            stockQty = edit_text_stockDetail_qty.text.toString().toIntOrNull()
            var valid: Boolean = true

            if (prodBarcode == null) {
                input_layout_stockDetail_ProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_stockDetail_ProdBarcode.error = null
            }

            if (stockQty == null) {
                input_layout_stockDetail_qty.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                input_layout_stockDetail_qty.error = null
            }

            if (valid) {
                var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode.toString())
                prodBarcodeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            valid = false
                            input_layout_prodSupplierName.error = "Invalid product barcode"
                        } else {
                            val stockDetail = StockDetail()
                            stockDetail.stockDetailProdBarcode = prodBarcode
                            stockDetail.stockDetailRackId = rackId
                            stockDetail.stockDetailQty = stockQty
                            stockDetail.stockTypeId = sharedStockInViewModel.stockTypePushKey.value
                            stockViewModel.addStockDetail(stockDetail)
                            requireActivity().supportFragmentManager.popBackStack("addStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

//            when {
//                rackId.isEmpty() -> {
//                    input_layout_prodSupplierName.error = getString(R.string.error_field_required)
//                    return@setOnClickListener
//                }
//                rowNo.isEmpty() -> {
//                    input_layout_prodDesc.error = getString(R.string.error_field_required)
//                    return@setOnClickListener
//                }
//                else -> {
//                    val stockDetail = StockDetail()
//                    stockDetail.stockDetailProdBarcode = prodBarcode.toInt()
//                    stockDetail.stockDetailRackId = rackId
//                    stockDetail.stockDetailRowNum = rowNo
//                    stockDetail.stockDetailQty = stockQty.toInt()
//                    //prodViewModel.stockUpdateProduct(prodBarcode, stockQty.toInt())
//                    stockDetail.stockTypeId = sharedStockInViewModel.stockTypePushKey.value
//                    // TODO add in the stockType Id
//                    stockViewModel.addStockDetail(stockDetail)
//                    requireActivity().supportFragmentManager.popBackStack("addStockDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                }
//            }

        btn_stockDetail_scanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment("product"))
            transaction.addToBackStack("productBarcode")
            transaction.commit()
        }

//        btn_stockDetail_scanRackId.setOnClickListener {
//            val currentView = (requireView().parent as ViewGroup).id
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(currentView, ScanBarcodeFragment("rack"))
//            transaction.addToBackStack("rackBarcode")
//            transaction.commit()
//        }

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

        val rackBarcodeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("check", "go into listener")
                populateSearchRackBarcode(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("check", "error")
                TODO("Not yet implemented")
            }
        }
        dbRack.addListenerForSingleValueEvent(rackBarcodeListener)

    }


    protected fun populateSearchProdBarcode(snapshot: DataSnapshot) {
        var prodBarcodes: ArrayList<String> = ArrayList<String>()
        if (snapshot.exists()) {
            snapshot.children.forEach {
                var prodBarcode: String = it.child("prodBarcode").value.toString()
                prodBarcodes.add(prodBarcode)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, prodBarcodes)
            edit_text_stockDetail_ProdBarcode.setAdapter(adapter)
        } else {
            Log.d("checkAuto", "No match found")
        }
    }

    protected fun populateSearchRackBarcode(snapshot: DataSnapshot) {
        var rackBarcodes: ArrayList<String> = ArrayList<String>()
        if (snapshot.exists()) {
            snapshot.children.forEach {
                var rackCodeStored: String = it.child("rackName").value.toString()
                rackBarcodes.add(rackCodeStored)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rackBarcodes)
            edit_text_stockDetail_rackId.setAdapter(adapter)
        } else {
            Log.d("checkAuto", "No match found")
        }
    }






//                if(barcodeStored == prodBarcode){
//                    Log.d("check same", "barcodeStored$barcodeStored")
//                    Log.d("check same", "prodBarcode$prodBarcode")
//                    var product = Product()
//                    product.prodQty = edit_text_stockDetail_qty.text.toString().toInt()
//                    product.prodId = snapshot.key
//                    dbProd.child(snapshot.key!!).setValue(product)
//                            .addOnCompleteListener {
//                                if (it.isSuccessful) {
//                                    Log.d("check","successful add in - prod update ")
//                                } else {
//                                    Log.d("check","fail add in - prod update")
//                                }
//                            }
//                }
//                else{
//                    Log.d("check Diff", "barcodeStored$barcodeStored")
//                    Log.d("check Diff", "prodBarcode$prodBarcode")
//                }
//            }


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
