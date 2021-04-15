package com.example.cc_mobileapp.stock.stockOut

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
import com.example.cc_mobileapp.stock.stockOutDetail.StockOutDetailFragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_stock_out_client.*

class StockOutClientFragment : Fragment() {
    private val dbClient = FirebaseDatabase.getInstance().getReference(Constant.NODE_CLIENT)
    private val sharedStockOutViewModel: StockOutViewModel by activityViewModels()
    lateinit var stockOutClientName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_out_client, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_continueAddStockOutDetails.setOnClickListener {
            stockOutClientName = edit_text_stockOut_clientName.text.toString().trim()
            var valid: Boolean = true
            if(stockOutClientName.isNullOrEmpty()) {
                input_layout_stockOut_clientName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }else{
                input_layout_stockOut_clientName.error = null
                valid = true
            }

            if(valid){
                var clientNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_CLIENT).orderByChild("clientCoName").equalTo(stockOutClientName)
                clientNameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            sharedStockOutViewModel.setStockOutClientId(stockOutClientName)
                            val typePushKey: String = sharedStockOutViewModel.generateTypePuskKey().toString()
                            val currentView = (requireView().parent as ViewGroup).id
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(currentView, StockOutDetailFragment())
                            transaction.addToBackStack("stockOutDetailFragment")
                            transaction.commit()
                        }else{
                            input_layout_stockOut_clientName.error = "Invalid client"
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        btn_cancelAddStockOutDetails.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("stockOutClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        // Autocomplete for product barcode
        val clientNameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                populateSearchClientName(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbClient.addListenerForSingleValueEvent(clientNameListener)
    }

    protected fun populateSearchClientName(snapshot: DataSnapshot) {
        var clientNames: ArrayList<String> = ArrayList<String>()
        if(snapshot.exists()){
            snapshot.children.forEach{
                var clientName: String = it.child("clientCoName").value.toString()
                clientNames.add(clientName)
            }
            var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, clientNames)
            edit_text_stockOut_clientName.setAdapter(adapter)
        }else{
            Log.d("checkAuto", "No match found")
        }
    }
}