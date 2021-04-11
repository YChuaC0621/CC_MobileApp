package com.example.cc_mobileapp.product

import android.app.AlertDialog
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
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_client.*
import kotlinx.android.synthetic.main.fragment_edit_product.*

class EditProductFragment(private val product: Product) : Fragment() {

    private lateinit var viewModel: ProductViewModel
    private val sharedBarcodeViewModel: ProductBarcodeViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@EditProductFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_text_editProductName.setText(product.prodName)
        edit_text_editSupplierName.setText(product.supplierId)
        edit_text_editProdDesc.setText(product.prodDesc)
        edit_text_editProdPrice.setText(product.prodPrice.toString())
        edit_text_editProdBarcode.setText(product.prodBarcode.toString())

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message:String
            if (it == null) {
                message = (R.string.client_added).toString()

            } else {
                message = getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show().toString()
            requireActivity().supportFragmentManager.popBackStack("editBarcodeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //dismiss()
        })


        btn_productConfirmEdit.setOnClickListener {
            val prodName = edit_text_editProductName.text.toString().trim()
            val supplierId = edit_text_editSupplierName.text.toString().trim()
            val prodDesc = edit_text_editProdDesc.text.toString().trim()
            val prodPrice = edit_text_editProdPrice.text.toString().trim()
            val prodBarcode = edit_text_editProdBarcode.text.toString().trim()
            when {
                prodName.isEmpty() -> {
                    input_layout_clientCoName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                supplierId.isEmpty() -> {
                    input_layout_clientEmail.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodDesc.isEmpty() -> {
                    input_layout_clientHp.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodPrice.isEmpty() -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodBarcode.isEmpty() -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    product.prodName = prodName
                    product.supplierId = supplierId
                    product.prodDesc = prodDesc
                    product.prodPrice = prodPrice.toDouble()
                    product.prodBarcode = prodBarcode.toInt()
                    Log.d("Check", "Update client data $product")
                    viewModel.updateProduct(product)
                }
            }
        }

        btn_productDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteProduct(product)
                }
            }.create().show()
        }

        btn_editScanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment())
            transaction.addToBackStack("editBarcode")
            transaction.commit()
        }

//        // Autocomplete for product barcode
//        val supplierNameListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                populateSearchSupplierName(snapshot)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        }
//        dbSupplier.addListenerForSingleValueEvent(supplierNameListener)
//    }
//
//    protected fun populateSearchSupplierName(snapshot: DataSnapshot) {
//        var supplierNames: ArrayList<String> = ArrayList<String>()
//        if(snapshot.exists()){
//            snapshot.children.forEach{
//                var supplierName: String = it.child("supplierName").value.toString()
//                supplierNames.add(supplierName)
//            }
//            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, supplierNames)
//            edit_text_editSupplierName.setAdapter(adapter)
//        }else{
//            Log.d("checkAuto", "No match found")
//        }
    }

    override fun onResume() {
        super.onResume()
        if(!sharedBarcodeViewModel.scannedCode.value.isNullOrEmpty()){
            edit_text_editProdBarcode.setText(sharedBarcodeViewModel.scannedCode.value)
            sharedBarcodeViewModel.clearBarcode()
        }
        else{
            Toast.makeText(requireContext(), "viewModel have nothing", Toast.LENGTH_SHORT).show()
        }
    }

}