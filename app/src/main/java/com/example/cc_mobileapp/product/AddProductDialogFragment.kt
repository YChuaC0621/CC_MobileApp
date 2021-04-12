package com.example.cc_mobileapp.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.Product
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.*
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.product_display_item.*

class AddProductDialogFragment : Fragment() {

    private lateinit var viewModel: ProductViewModel
    private val sharedBarcodeViewModel: ProductBarcodeViewModel by activityViewModels()
//    private val dbSupplier = FirebaseDatabase.getInstance().getReference(Constant.NODE_SUPPLIER)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@AddProductDialogFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_product_dialog, container, false)
    }

    var prodName: String = ""
    var supplierName: String = ""
    var prodDesc: String = ""
    var prodPrice: Double = 0.0
    var prodBarcode: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it==null){
                getString(R.string.prodAddSuccess)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_addProdInDialog.setOnClickListener {
            prodName = edit_text_prodName.text.toString().trim()
            supplierName = edit_text_prodSupplierName.text.toString().trim()
            prodDesc = edit_text_prodDesc.text.toString().trim()
            prodPrice = edit_text_prodPrice.text.toString().trim().toDouble()
            prodBarcode = edit_text_prodBarcode.text.toString().trim().toInt()
            var valid: Boolean = true

            if(prodName.isEmpty()){
                input_layout_prodName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodName.error = null
            }

            // TODO check exists
            if(supplierName.isEmpty()){
                input_layout_prodSupplierName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if(!viewModel.checkSupplierExist(supplierName)){
                input_layout_prodSupplierName.error = "Supplier does not exists"
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodSupplierName.error = null
            }

            if(prodDesc.isEmpty()){
                input_layout_prodDesc.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodDesc.error = null
            }

            if(prodPrice == 0.0){
                input_layout_prodPrice.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodPrice.error = null
            }

            // TODO cannot exits
            if(prodBarcode == 0){
                input_layout_prodBarcode.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_prodBarcode.error = null
            }

            if(valid){
                Log.d("Check", "before val product")
                val product = Product()
                Log.d("Check", "after val product")
                product.prodName = prodName
                product.supplierId = supplierName
                product.prodDesc = prodDesc
                product.prodPrice = prodPrice.toDouble()
                product.prodBarcode = prodBarcode.toInt()
                product.prodQty = 0
                Log.d("Check", "client data $product")
                viewModel.addProduct(product)
                requireActivity().supportFragmentManager.popBackStack("addBarcodeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

        btn_addProdScanBarcode.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, ScanBarcodeFragment())
            transaction.addToBackStack("addBarcode")
            transaction.commit()
        }

        btn_addProdBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("addBarcodeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
//            edit_text_prodSupplierName.setAdapter(adapter)
//        }else{
//            Log.d("checkAuto", "No match found")
//        }


    }



    override fun onResume() {
        super.onResume()
        if(!sharedBarcodeViewModel.scannedCode.value.isNullOrEmpty()){
            edit_text_prodBarcode.setText(sharedBarcodeViewModel.scannedCode.value)
            sharedBarcodeViewModel.clearBarcode()
        }
        else{
            Toast.makeText(requireContext(), "viewModel have nothing", Toast.LENGTH_SHORT).show()
        }
    }

}