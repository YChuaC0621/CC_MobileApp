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

    // variable
    private lateinit var viewModel: ProductViewModel
    private val sharedBarcodeViewModel: ProductBarcodeViewModel by activityViewModels()
    private val dbSupplier = FirebaseDatabase.getInstance().getReference(Constant.NODE_SUPPLIER)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // provide this product with client view model information
        viewModel = ViewModelProvider(this@EditProductFragment).get(ProductViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // set the information from the clicked recycler item to the autocomplete input text
        edit_text_editProductName.setText(product.prodName)
        edit_text_editSupplierName.setText(product.supplierName)
        edit_text_editProdDesc.setText(product.prodDesc)
        edit_text_editProdPrice.setText(product.prodPrice.toString().trim())
        edit_text_editProdBarcode.setText(product.prodBarcode)
        lateinit var prodName: String
        lateinit var supplierName: String
        lateinit var prodDesc: String
        var prodPrice: Double? = null
        lateinit var prodBarcode: String

        // observe if any changes on the view model result variable
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message:String
            if (it == null) {
                message = getString(R.string.prod_delete_success)
            } else {
                message = getString(R.string.error, it.message)
            }
            //after changes has been made, a toast of the status will be on text
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show().toString()
            // go back to previous fragment
            requireActivity().supportFragmentManager.popBackStack("editBarcodeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        // when user click on "+" button
        btn_productConfirmEdit.setOnClickListener {
            // retrieve and update client information from input text
            prodName = edit_text_editProductName.text.toString().trim()
            supplierName = edit_text_editSupplierName.text.toString().trim()
            prodDesc = edit_text_editProdDesc.text.toString().trim()
            prodPrice = edit_text_editProdPrice.text.toString().trim().toDoubleOrNull()
            prodBarcode = edit_text_editProdBarcode.text.toString().trim()
            var valid: Boolean = true

            // validation
            // validate product name
            if(prodName.isNullOrEmpty()){
                input_layout_editProductName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProductName.error = null
            }

            // validation on supplier name
            if(supplierName.isNullOrEmpty()){
                input_layout_editSupplierName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else {
                input_layout_editSupplierName.error = null
            }

            // validation on product description
            if(prodDesc.isNullOrEmpty()){
                input_layout_editProdDesc.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdDesc.error = null
            }

            // validation on product price
            if(prodPrice == null){
                input_layout_editProdPrice.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }else if (prodPrice!!.equals(0.0)){
                input_layout_editProdPrice.error =getString(R.string.prodPrice_nonzero_error)
                valid = false
                return@setOnClickListener
            }
            else if(!checkRegexPrice(prodPrice.toString())){
                input_layout_prodPrice.error = getString(R.string.only_priceformat_error)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdPrice.error = null
            }

            // validation on product barcode
            if(prodBarcode.isNullOrEmpty()){
                input_layout_editProdBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if(!checkRegexBarcode(prodBarcode)){
                input_layout_editProdBarcode.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editProdBarcode.error = null
            }

            // further validation on product availability to be create with information from database
            if(valid){
                // create new product
                val newProduct = Product()
                newProduct.prodId = product.prodId
                newProduct.prodName = prodName
                newProduct.supplierName = supplierName
                newProduct.prodDesc = prodDesc
                newProduct.prodPrice = prodPrice!!.toDouble()
                newProduct.prodBarcode = prodBarcode.trim()

                /// check on the exist of the supplier name
                var supplierNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_SUPPLIER).orderByChild("supCmpName").equalTo(supplierName)
                supplierNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists()){
                            valid = false
                            input_layout_editSupplierName.error = getString(R.string.invalid_nonexist_error)
                        }else{
                            // check whether information is not change when edit button is clicked
                            if(newProduct.prodBarcode == product.prodBarcode){
                                if(newProduct.prodPrice!! != product.prodPrice || newProduct.prodName!! != product.prodName || newProduct.prodDesc!! != product.prodDesc || newProduct.supplierName!! != product.supplierName){
                                    // if information changes, update the product information
                                    viewModel.updateProduct(newProduct)
                                }
                                else{
                                    Toast.makeText(requireContext(), getString(R.string.prodedit_info_remain), Toast.LENGTH_SHORT).show()
                                }
                                // go back to previous fragment
                                requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                            else{
                                // check on the duplication of product barcode
                                var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode)
                                prodBarcodeQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(!snapshot.exists()){
                                            // product barcode is duplicated, check on whether the product information is changed
                                            if(newProduct.prodPrice!! != product.prodPrice || newProduct.prodBarcode!! != product.prodBarcode || newProduct.prodName!! != product.prodName || newProduct.prodDesc!! != product.prodDesc || newProduct.supplierName!! != product.supplierName){
                                                viewModel.updateProduct(newProduct)
                                                Toast.makeText(requireContext(), getString(R.string.prodInfo_success), Toast.LENGTH_SHORT).show()
                                            }
                                            else{
                                                Toast.makeText(requireContext(), getString(R.string.prodedit_info_remain), Toast.LENGTH_SHORT).show()
                                            }
                                            requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        }else {
                                            valid = false
                                            // display error on the input layout for errors
                                            input_layout_editProdBarcode.error = getString(R.string.exist_prodBarcode_error)
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
        // if "cancel" button is click, go back to previous fragment
        btn_editProdCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // if "delete" button is click, display alert dialog and delete the product if yes has been selected
        btn_productDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteProduct(product)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
            }.create().show()
            requireActivity().supportFragmentManager.popBackStack("editProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // go to the scan barcode fragment
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

    // regex validation for barcode
    private fun checkRegexBarcode(prodBarcode: String): Boolean {
        var prodBarcode: String = prodBarcode
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = prodBarcode)
    }

    // regex validation for price
    private fun checkRegexPrice(prodPrice: String): Boolean {
        var prodPrice: String = prodPrice
        var regex:Regex = Regex(pattern="^\\d{0,9}\\.\\d{1,2}$")
        return regex.matches(input = prodPrice)
    }

    // Auto complete for supplier name
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

    // when returned from the scanning product, retrieve the information from the shared barcode view model to bind on the text box
    override fun onResume() {
        super.onResume()
        if(!sharedBarcodeViewModel.scannedCode.value.isNullOrEmpty()){
            edit_text_editProdBarcode.setText(sharedBarcodeViewModel.scannedCode.value)
            sharedBarcodeViewModel.clearBarcode()
        }
    }

}