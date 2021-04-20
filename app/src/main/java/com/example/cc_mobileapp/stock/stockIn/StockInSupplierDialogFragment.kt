package com.example.cc_mobileapp.stock.stockIn

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.stock.stockDetail.StockDetailFragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_stock_detail.*
import kotlinx.android.synthetic.main.fragment_stock_in___main.*
import kotlinx.android.synthetic.main.fragment_stock_in_supplier_dialog.*

class StockInSupplierDialogFragment : Fragment(){
    // variable declaration
    private val dbSupplier = FirebaseDatabase.getInstance().getReference(Constant.NODE_SUPPLIER)
    private val sharedStockInViewModel: StockInViewModel by activityViewModels()
    lateinit var stockInSupplierName: String
    private lateinit var supplierNameListener:ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_in_supplier_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // button "next" is clicked, validation on the supplier name
        btn_continueAddStockInDetails.setOnClickListener {
            stockInSupplierName = edit_text_stockIn_supplierName.text.toString().trim()
            var valid: Boolean = true

            // validation on supplier name
            if(stockInSupplierName.isNullOrEmpty()) {
                input_layout_stockIn_supplierName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }else{
                input_layout_stockIn_supplierName.error = null
                valid = true
            }

            if(valid){
                // check if supplier name exist
                var supplierNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_SUPPLIER).orderByChild("supCmpName").equalTo(stockInSupplierName)
                supplierNameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            sharedStockInViewModel.setStockInSupplierId(stockInSupplierName)
                            val typePushKey: String = sharedStockInViewModel.generateTypePushKey().toString()
                            val currentView = (requireView().parent as ViewGroup).id
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(currentView, StockDetailFragment())
                            transaction.addToBackStack("stockInDetailFragment")
                            transaction.commit()
                        }else{
                            input_layout_stockIn_supplierName.error = getString(R.string.invalid_nonexist_error)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
        // when "cancel" button is click, go back to previous fragment
        btn_cancelAddStockInDetails.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("stockInSupplierDialogFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        // Autocomplete for supplier name
        supplierNameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                populateSearchSupplierName(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbSupplier.addListenerForSingleValueEvent(supplierNameListener)
    }

    // autocomplete for supplier name
    protected fun populateSearchSupplierName(snapshot: DataSnapshot) {
        var supplierNames: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var supplierName: String = it.child("supCmpName").value.toString()
                supplierNames.add(supplierName)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, supplierNames)
            edit_text_stockIn_supplierName.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbSupplier.removeEventListener(supplierNameListener)
    }

}