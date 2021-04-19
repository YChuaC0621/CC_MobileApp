package com.example.cc_mobileapp.supplier

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_supplier.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.btn_Save
import kotlinx.android.synthetic.main.fragment_edit_supplier.editTxt_supCmpLot
import kotlinx.android.synthetic.main.fragment_edit_supplier.editTxt_supCmpName
import kotlinx.android.synthetic.main.fragment_edit_supplier.editTxt_supEmail
import kotlinx.android.synthetic.main.fragment_edit_supplier.editTxt_supHpNum
import kotlinx.android.synthetic.main.fragment_edit_supplier.editTxt_supName
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtInputLayout_supCmpLot
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtInputLayout_supCmpName
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtInputLayout_supEmail
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtInputLayout_supHpNum
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtInputLayout_supName

class EditSupplierFragment(private val supplier: Supplier) : Fragment() {

    //Data declaration
    private lateinit var  viewModel: SupplierViewModel

    //Bind view model to view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@EditSupplierFragment).get(SupplierViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_supplier, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //set the text field and display for user
        editTxt_supName.setText(supplier.supName)
        editTxt_supEmail.setText(supplier.supEmail)
        editTxt_supHpNum.setText(supplier.supHpNum)
        editTxt_supCmpName.setText(supplier.supCmpName)
        editTxt_supCmpLot.setText(supplier.supCmpLot)

        //check the update results
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message:String
            if (it == null) {
                message = (R.string.supplier_edited).toString()
            } else {
                message = getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show().toString()
            requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        })

        //update the information to database
        btn_Save.setOnClickListener {
            val supName = editTxt_supName.text.toString().trim()
            val supEmail = editTxt_supEmail.text.toString().trim()
            val supHpNum = editTxt_supHpNum.text.toString().trim()
            val supCmpName = editTxt_supCmpName.text.toString().trim()
            val supCmpLot = editTxt_supCmpLot.text.toString().trim()

            //validation
            var valid = true
            if(supName.isEmpty()) {
                txtInputLayout_supName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                txtInputLayout_supName.error = null
            }
                if(supEmail.isEmpty()) {
                    txtInputLayout_supEmail.error = getString(R.string.error_field_required)
                    valid = false
                    return@setOnClickListener
                }
                else{
                    txtInputLayout_supEmail.error = null
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(supEmail).matches()){
                    valid = false
                    txtInputLayout_supEmail.error = getString(R.string.errorEmail)
                    return@setOnClickListener
                }
                else{
                    txtInputLayout_supEmail.error = null
                }

                if(supHpNum.isEmpty()){
                    txtInputLayout_supHpNum.error = getString(R.string.error_field_required)
                    valid = false
                    return@setOnClickListener
                } else if(!checkRegexhpNum(supHpNum)){
                    txtInputLayout_supHpNum.error = getString(R.string.phone_format_error)
                    valid = false
                    return@setOnClickListener
                }else{
                    txtInputLayout_supHpNum.error = null
                }

                if(supCmpName.isEmpty()){
                    txtInputLayout_supCmpName.error = getString(R.string.error_field_required)
                    valid = false
                    return@setOnClickListener
                }
                else{
                    txtInputLayout_supCmpName.error = null
                }

                if(supCmpLot.isEmpty()){
                    txtInputLayout_supCmpLot.error = getString(R.string.error_field_required)
                    valid = false
                    return@setOnClickListener
                }
                else{
                    txtInputLayout_supCmpLot.error = null
                }

            //if all valid inputs update to database
               if(valid){
                    supplier.supName = supName
                    supplier.supEmail = supEmail
                    supplier.supHpNum = supHpNum
                    supplier.supCmpName = supCmpName
                    supplier.supCmpLot = supCmpLot
                    Log.d("Check", "Update client data $supplier")
                    viewModel.updateSuppliers(supplier)
                }
            }


        //delete the supplier from database
        txtDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteSupplier(supplier)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
            }.create().show()
        }
    }

    //make sure the phone number is in valid format
    private fun checkRegexhpNum(hpNum: String): Boolean {
        var hpNum: String = hpNum
        var regex: Regex = Regex(pattern = """\d+""")
        return regex.matches(input = hpNum) && hpNum.startsWith("01")
    }
}