package com.example.cc_mobileapp.product

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
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_edit_product.*
import kotlinx.android.synthetic.main.product_display_item.*

class AddProductDialogFragment : Fragment() {

    // variable
    private lateinit var viewModel: ProductViewModel
    private val sharedBarcodeViewModel: ProductBarcodeViewModel by activityViewModels()
    private val dbSupplier = FirebaseDatabase.getInstance().getReference(Constant.NODE_SUPPLIER)
    lateinit var prodName: String
    lateinit var supplierName: String
    lateinit var prodDesc: String
    var prodPrice: Double? = null
    lateinit var prodBarcode: String
    private lateinit var supplierNameListener:ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // provide this product with client view model information
        viewModel = ViewModelProvider(this@AddProductDialogFragment).get(ProductViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // observe if any changes on the view model result variable
        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it==null){
                getString(R.string.prodAddSuccess)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        // if " +" button is clicked,
        btn_addProdInDialog.setOnClickListener {
            prodName = edit_text_prodName.text.toString().trim()
            supplierName = edit_text_prodSupplierName.text.toString().trim()
            prodDesc = edit_text_prodDesc.text.toString().trim()
            prodPrice = edit_text_prodPrice.text.toString().format("%.2f").toDoubleOrNull()
            prodBarcode = edit_text_prodBarcode.text.toString().trim()

            // validation on input data
            var valid: Boolean = true
            // products company name validation
            if(prodName.isNullOrEmpty()){
                input_layout_prodName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodName.error = null
            }

            // validation on product description
            if(prodDesc.isNullOrEmpty()){
                input_layout_prodDesc.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodDesc.error = null
            }

            // check supplier name validation
            if(supplierName.isNullOrEmpty()){
                input_layout_prodSupplierName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else {
                input_layout_prodSupplierName.error = null
            }

            // check product price destination
            if(prodPrice == null){
                input_layout_prodPrice.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }else if (prodPrice!!.equals(0.0)){
                input_layout_prodPrice.error = getString(R.string.prodPrice_nonzero_error)
                valid = false
                return@setOnClickListener
            }else if(!checkRegexPrice(prodPrice.toString())){
                input_layout_prodPrice.error = getString(R.string.only_priceformat_error)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodPrice.error = null
            }

            // validation on product barcode
            if(prodBarcode.isNullOrEmpty()){
                input_layout_prodBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if(!checkRegexBarcode(prodBarcode)){
                input_layout_prodBarcode.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodBarcode.error = null
            }

            if(valid){
                // retrieve data from supplier with match supplier entered
                var supplierNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_SUPPLIER).orderByChild("supCmpName").equalTo(supplierName)
                supplierNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // if no record found
                        if(!snapshot.exists()){
                            valid = false
                            input_layout_prodSupplierName.error = getString(R.string.invalid_nonexist_error)
                        }else{
                            // retrieve product barcode
                            var prodBarcodeQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_PRODUCT).orderByChild("prodBarcode").equalTo(prodBarcode)
                            prodBarcodeQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(!snapshot.exists()){
                                        // construct a new product
                                        val product = Product()
                                        product.prodName = prodName
                                        product.supplierName = supplierName
                                        product.prodDesc = prodDesc
                                        product.prodPrice = prodPrice!!.toDouble()
                                        product.prodBarcode = prodBarcode
                                        product.prodQty = 0
                                        // add product
                                        viewModel.addProduct(product)
                                        // pop backstack
                                        Toast.makeText(requireContext(), "Successfully Add Product", Toast.LENGTH_SHORT).show()
                                        requireActivity().supportFragmentManager.popBackStack("addProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    }else {
                                        valid = false
                                        input_layout_prodBarcode.error = getString(R.string.exist_prodBarcode_error)
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

        // if "scan barcode" is clicked, redirect user to using scanning to input value
        btn_addProdScanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment())
            transaction.addToBackStack("addBarcode")
            transaction.commit()
        }

        // if click "cancel" button, pop user back to previous fragment
        btn_addProdBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("addProductFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    // validation on product price
    private fun checkRegexPrice(prodPrice: String): Boolean {
        var prodPrice: String = prodPrice
        var regex:Regex = Regex(pattern="^\\d{0,9}\\.\\d{1,2}$")
        return regex.matches(input = prodPrice)
    }

    // validation on barcode entered
    private fun checkRegexBarcode(prodBarcode: String): Boolean {
        var prodBarcode: String = prodBarcode
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = prodBarcode)
    }

    // validation on check the availability
    protected fun populateSearchSupplierName(snapshot: DataSnapshot) {
        // check name availability
        var supplierNames: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var supplierName: String = it.child("supCmpName").value.toString()
                supplierNames.add(supplierName)
            }
            var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, supplierNames)
            edit_text_prodSupplierName.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
        }
    }

    // when returned from the scanning product, retrieve the information from the shared barcode view model to bind on the text box
    override fun onResume() {
        super.onResume()
        if(!sharedBarcodeViewModel.scannedCode.value.isNullOrEmpty()){
            edit_text_prodBarcode.setText(sharedBarcodeViewModel.scannedCode.value)
            sharedBarcodeViewModel.clearBarcode()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbSupplier.removeEventListener(supplierNameListener)
    }

}
