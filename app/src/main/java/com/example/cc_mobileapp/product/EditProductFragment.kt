package com.example.cc_mobileapp.product

import android.app.AlertDialog
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
import com.example.cc_mobileapp.model.Product
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_product.*

class EditProductFragment(private val product: Product) : Fragment() {

    private lateinit var viewModel: ProductViewModel
    private val sharedBarcodeViewModel: ProductBarcodeViewModel by activityViewModels()
    private val dbSupplier = FirebaseDatabase.getInstance().getReference(Constant.NODE_SUPPLIER)


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
        edit_text_editSupplierName.setText(product.supplierName)
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

        lateinit var prodName: String
        lateinit var supplierName: String
        lateinit var prodDesc: String
        var prodPrice: Double? = null
        var prodBarcode: Int? = null
        btn_productConfirmEdit.setOnClickListener {
            prodName = edit_text_editProductName.text.toString().trim()
            supplierName = edit_text_editSupplierName.text.toString().trim()
            prodDesc = edit_text_editProdDesc.text.toString().trim()
            prodPrice = edit_text_editProdPrice.text.toString().toDoubleOrNull()
            prodBarcode = edit_text_editProdBarcode.text.toString().toIntOrNull()
            var valid: Boolean = true

            if(prodName.isNullOrEmpty()){
                input_layout_editProductName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProductName.error = null
            }

            if(prodDesc.isNullOrEmpty()){
                input_layout_editProdDesc.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdDesc.error = null
            }

            if(supplierName.isNullOrEmpty()){
                input_layout_editSupplierName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else {
                input_layout_editSupplierName.error = null
            }

            if(prodPrice == null){
                input_layout_editProdPrice.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdPrice.error = null
            }

            if(prodBarcode == null){
                input_layout_editProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdBarcode.error = null
            }

            if(valid){

                var supplierNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_SUPPLIER).orderByChild("supCmpName").equalTo(supplierName)
                supplierNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists()){
                            valid = false
                            input_layout_editSupplierName.error = "Invalid supplier"
                        }else{
                            val newProduct = Product()
                            newProduct.prodId = product.prodId
                            newProduct.prodName = prodName
                            newProduct.supplierName = supplierName
                            newProduct.prodDesc = prodDesc
                            newProduct.prodPrice = prodPrice!!.toDouble()
                            newProduct.prodBarcode = prodBarcode!!.toInt()

                            if(newProduct.prodPrice!! != product.prodPrice || newProduct.prodBarcode!! != product.prodBarcode || newProduct.prodName!! != product.prodName || newProduct.prodDesc!! != product.prodDesc || newProduct.supplierName!! != product.supplierName){
                                viewModel.updateProduct(newProduct)
                            }
                            else{
                                Toast.makeText(requireContext(), "Product information remain unchanged", Toast.LENGTH_SHORT).show()
                            }
                            requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        btn_productDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteProduct(product)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
            }.create().show()
        }

        btn_editScanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment())
            transaction.addToBackStack("editBarcode")
            transaction.commit()
        }

        // Autocomplete for product barcode
        val supplierNameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                populateSearchSupplierName(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbSupplier.addListenerForSingleValueEvent(supplierNameListener)
    }

    protected fun populateSearchSupplierName(snapshot: DataSnapshot) {
        var supplierNames: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var supplierName: String = it.child("supCmpName").value.toString()
                supplierNames.add(supplierName)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, supplierNames)
            edit_text_editSupplierName.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
        }
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