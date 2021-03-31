package com.example.cc_mobileapp.supplier

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Supplier
import kotlinx.android.synthetic.main.fragment_add_supplier.*

class AddSupplierDialogFragment  : Fragment() {

    private lateinit var viewModel: SupplierViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@AddSupplierDialogFragment).get(SupplierViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_supplier, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it == null) {// success
                getString(R.string.supplier_added)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_Save.setOnClickListener{
            val supName = editTxt_supName.text.toString().trim()
            val supEmail = editTxt_supEmail.text.toString().trim()
            val supHpNum = editTxt_supHpNum.text.toString().trim()
            val supCmpName = editTxt_supCmpName.text.toString().trim()
            val supCmpLot = editTxt_supCmpLot.text.toString().trim()

            when {
                supName.isEmpty() -> {
                    txtInputLayout_supName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                supEmail.isEmpty() -> {
                    txtInputLayout_supEmail.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(supEmail).matches() -> {
                    txtInputLayout_supEmail.error = getString(R.string.errorEmail)
                    return@setOnClickListener
                }
                supHpNum.isEmpty() -> {
                    txtInputLayout_supHpNum.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                !android.util.Patterns.PHONE.matcher(supHpNum).matches() -> {
                    txtInputLayout_supEmail.error = getString(R.string.errorPhoneNum)
                    return@setOnClickListener
                }
                supCmpName.isEmpty() -> {
                    txtInputLayout_supCmpName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                supCmpLot.isEmpty() -> {
                    txtInputLayout_supCmpLot.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    val sup = Supplier()
                    sup.supName = supName
                    sup.supEmail = supEmail
                    sup.supHpNum = supHpNum
                    sup.supCmpName = supCmpName
                    sup.supCmpLot = supCmpLot
                    Log.d("Check", "supplier data $sup")
                    viewModel.addSupplier(sup)
                    requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }
}