package com.example.cc_mobileapp.rack

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
import com.example.cc_mobileapp.model.Rack
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_rack.*

class AddRackDialogFragment  : Fragment() {
    private lateinit var viewModel: RackViewModel

    //link view model with the view
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@AddRackDialogFragment).get(RackViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_rack, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //observe the results for add rack process
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {// success
                getString(R.string.rack_added)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        //Add Rack
        btn_Save.setOnClickListener {
            val rackNum = editTxt_rackNum.text.toString().trim()
            val startLot = editTxt_startLot.text.toString().trim()
            val endLot = editTxt_endLot.text.toString().trim()
            val row_num = editTxt_rowNum.text.toString().trim()
            var valid = true

            //validation for each field, row number only accept integer
            if (rackNum.isEmpty()) {
                txtInputLayout_rackName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                txtInputLayout_rackName.error = null
            }
            if (startLot.isEmpty()) {
                txtInputLayout_startLot.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                txtInputLayout_startLot.error = null
            }
            if (endLot.isEmpty()) {
                txtInputLayout_endLot.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                txtInputLayout_endLot.error = null
            }
            if (row_num.isEmpty()) {
                txtInputLayout_rowNum.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if (!checkRegexRowNum(row_num)) {
                txtInputLayout_rowNum.error = getString(R.string.only_integer_error)
                valid = false
                return@setOnClickListener
            }
            else if(!row_num.equals("1") || !row_num.equals("2"))
            {
                txtInputLayout_rowNum.error = getString(R.string.rowNum_error)
                valid = false
                return@setOnClickListener
            }
            else{
                txtInputLayout_rowNum.error = null
            }

            //if all inputs are valid, start to add to database
            if (valid) {
                val rackName = rackNum + "_Row" + row_num.toInt()
                val rack = Rack()
                rack.rackName = rackName
                rack.rackNum = rackNum
                rack.startLot = startLot
                rack.endLot = endLot
                rack.row_num = row_num.toInt()
                rack.prodId = "0"
                rack.currentQty = 0
                Log.d("Check", "rack data $rack")

                //pass to view model to add
                viewModel.addRack(rack)

                //allow the fragment navigate back to activity
                requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    //check the input is only integer
    private fun checkRegexRowNum(RowNum: String): Boolean {
        var RowNum: String = RowNum
        var regex: Regex = Regex(pattern = """\d+""")
        return regex.matches(input = RowNum)
    }
}

