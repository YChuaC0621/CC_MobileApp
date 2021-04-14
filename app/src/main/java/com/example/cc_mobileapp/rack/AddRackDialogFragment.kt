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
import kotlinx.android.synthetic.main.fragment_add_rack.*

class AddRackDialogFragment  : Fragment() {
    private lateinit var viewModel: RackViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@AddRackDialogFragment).get(RackViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_rack, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it == null) {// success
                getString(R.string.rack_added)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_Save.setOnClickListener{
            val rackNum = editTxt_rackNum.text.toString().trim()
            val startLot = editTxt_startLot.text.toString().trim()
            val endLot = editTxt_endLot.text.toString().trim()
            val row_num = editTxt_rowNum.text.toString().trim()

            when {
                rackNum.isEmpty() -> {
                    txtInputLayout_rackName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                startLot.isEmpty() -> {
                    txtInputLayout_startLot.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                endLot.isEmpty() -> {
                    txtInputLayout_endLot.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                row_num.isEmpty() -> {
                    txtInputLayout_rowNum.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
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
                    viewModel.addRack(rack)
                    requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }
}