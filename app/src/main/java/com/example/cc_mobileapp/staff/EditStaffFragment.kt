package com.example.cc_mobileapp.staff

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
import com.example.cc_mobileapp.model.Users
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_staff.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.btn_Save
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtDelete

class EditStaffFragment (private val staff: Users) : Fragment() {

    private lateinit var viewModel: StaffViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@EditStaffFragment).get(StaffViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_staff, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        editTxt_staffName.setText(staff.userName)
        editTxt_staffEmail.setText(staff.userEmail)
        editTxt_staffHpNum.setText(staff.userHpNum)
        if(staff.workingPosition == 1)
        {
            editTxt_working_position.setText("Staff")
        }
        else
            editTxt_working_position.setText("Top Management")

        editTxt_working_status.setText(staff.workingStatus.toString())
        editTxt_staffName.isEnabled = false
        editTxt_staffEmail.isEnabled = false
        editTxt_staffHpNum.isEnabled = false
        editTxt_working_status.isEnabled = false

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message: String
            if (it == null) {
                message = (R.string.staff_edited).toString()
            } else {
                message = getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show().toString()
            requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        })

        btn_Save.setOnClickListener {
            val working_pos = editTxt_working_position.text.toString()
            var valid = true
            if (working_pos.isEmpty()) {
                txtInputLayout_working_pos.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                txtInputLayout_working_pos.error = null
            }

            if (!(working_pos.equals("Staff") || working_pos.equals("Top Management"))) {
                txtInputLayout_working_pos.error = getString(R.string.error_pos)
                valid = false
                return@setOnClickListener
            } else {
                txtInputLayout_working_pos.error = null
            }

            if (valid) {
                if(working_pos.equals("Staff"))
                {
                    staff.workingPosition = 1
                }
                else
                    staff.workingPosition = 2
                Log.d("Check", "Update staff data $staff")
                viewModel.updateStaff(staff)
            }
        }


        txtDelete.setOnClickListener {
            AlertDialog.Builder(requireContext()).also {
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    viewModel.deleteStaff(staff)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
            }.create().show()
        }
    }
}