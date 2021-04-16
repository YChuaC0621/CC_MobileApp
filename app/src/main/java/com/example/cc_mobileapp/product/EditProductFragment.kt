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
import kotlinx.android.synthetic.main.fragment_edit_stock_detail.*

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
        edit_text_editProdBarcode.setText(product.prodBarcode)

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
        lateinit var prodBarcode: String
        btn_productConfirmEdit.setOnClickListener {
            prodName = edit_text_editProductName.text.toString().trim()
            supplierName = edit_text_editSupplierName.text.toString().trim()
            prodDesc = edit_text_editProdDesc.text.toString().trim()
            prodPrice = edit_text_editProdPrice.text.toString().toDoubleOrNull()
            prodBarcode = edit_text_editProdBarcode.text.toString()
            var valid: Boolean = true

            if(prodName.isNullOrEmpty()){
                input_layout_editProductName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProductName.error = null
            }

            if(supplierName.isNullOrEmpty()){
                input_layout_editSupplierName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else {
                input_layout_editSupplierName.error = null
            }

            if(prodDesc.isNullOrEmpty()){
                input_layout_editProdDesc.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdDesc.error = null
            }

            if(prodPrice == null){
                input_layout_editProdPrice.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }else if (prodPrice!!.equals(0.0)){
                input_layout_editProdPrice.error = "Price cannot be 0"
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdPrice.error = null
            }

            if(prodBarcode.isNullOrEmpty()){
                input_layout_editProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if(!checkRegexBarcode(prodBarcode)){
                input_layout_editProdBarcode.error = "Only integer are allowed"
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdBarcode.error = null
            }

            if(valid){
                val newProduct = Product()
                newProduct.prodId = product.prodId
                newProduct.prodName = prodName
                newProduct.supplierName = supplierName
                newProduct.prodDesc = prodDesc
                newProduct.prodPrice = prodPrice!!.toDouble()
                newProduct.prodBarcode = prodBarcode

                var supplierNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_SUPPLIER).orderByChild("supCmpName").equalTo(supplierName)
                supplierNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists()){
                            valid = false
                            input_layout_editSupplierName.error = "Invalid supplier"
                        }else{
                            if(newProduct.prodBarcode == product.prodBarcode){
                                if(newProduct.prodPrice!! != product.prodPrice || newProduct.prodName!! != product.prodName || newProduct.prodDesc!! != product.prodDesc || newProduct.supplierName!! != product.supplierName){
                                    viewModel.updateProduct(newProduct)
                                }
                                else{
                                    Toast.makeText(requireContext(), "Product information remain unchanged", Toast.LENGTH_SHORT).show()
                                }
                                requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                            else{
                                var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode)
                                prodBarcodeQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(!snapshot.exists()){
                                            if(newProduct.prodPrice!! != product.prodPrice || newProduct.prodBarcode!! != product.prodBarcode || newProduct.prodName!! != product.prodName || newProduct.prodDesc!! != product.prodDesc || newProduct.supplierName!! != product.supplierName){
                                                viewModel.updateProduct(newProduct)
                                            }
                                            else{
                                                Toast.makeText(requireContext(), "Product information remain unchanged", Toast.LENGTH_SHORT).show()
                                            }
                                            requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        }else {
                                            valid = false
                                            input_layout_editProdBarcode.error = "Existing product barcode"
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
            requireActivity().supportFragmentManager.popBackStack("editBarcodeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    private fun checkRegexBarcode(prodBarcode: String): Boolean {
        var prodBarcode: String = prodBarcode
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = prodBarcode)
    }

    protected fun populateSearchSupplierName(snapshot: DataSnapshot) {
        var supplierNames: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var supplierName: String = it.child("supCmpName").value.toString()
                supplierNames.add(supplierName)
            }
            var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, supplierNames)
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
    }

}