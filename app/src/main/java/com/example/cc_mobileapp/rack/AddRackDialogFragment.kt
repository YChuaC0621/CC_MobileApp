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
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Rack
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_rack.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddRackDialogFragment  : Fragment() {
    private lateinit var viewModel: RackViewModel
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)

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
            } else {
                txtInputLayout_rackName.error = null
            }
            if (startLot.isEmpty()) {
                txtInputLayout_startLot.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                txtInputLayout_startLot.error = null
            }
            if (endLot.isEmpty()) {
                txtInputLayout_endLot.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
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
            } else if (row_num != "1" && row_num != "2") {
                txtInputLayout_rowNum.error = getString(R.string.rowNum_error)
                valid = false
                return@setOnClickListener
            } else {
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

                viewModel.fetchRackDetails()
                viewModel.racks.observe(viewLifecycleOwner, Observer {
                        Log.d("Check", "reach checking")
                        checkRack(rack, it)
                })
}
        }
    }

    //check the input is only integer
    private fun checkRegexRowNum(RowNum: String): Boolean {
        var RowNum: String = RowNum
        var regex: Regex = Regex(pattern = """\d+""")
        return regex.matches(input = RowNum)
    }

    private fun checkRack(rack: Rack, racks: List<Rack>) {

        //observe the results for add rack process

        //access rack database
        var found = 0
        for (checking in racks) {
            if (checking?.rackName == rack?.rackName) {
                Log.d("Check", "reach checking name ${checking?.rackName}, ${rack?.rackName}")
                txtInputLayout_rackName.error = getString(R.string.existRackMsg)
                txtInputLayout_startLot.error = null
                found = 1
            } else if ((checking?.startLot == rack?.startLot) && (checking?.endLot == rack?.endLot) && (checking?.rackNum != rack?.rackNum)) {
                Log.d("Check", "reach checking location ${checking?.startLot}, ${rack?.startLot}")
                Log.d("Check", "reach checking location ${checking?.endLot}, ${rack?.endLot}")
                txtInputLayout_startLot.error = getString(R.string.existRackLotMsg)
                txtInputLayout_rackName.error = null
                found = 1
            }
        }
        if (found == 0) {
            //pass to view model to add
            viewModel.addRack(rack)
            Toast.makeText(requireContext(), getString(R.string.rack_added), Toast.LENGTH_SHORT).show()
            //allow the fragment navigate back to activity
            requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

    }

}

