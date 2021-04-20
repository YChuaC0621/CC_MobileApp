package com.example.cc_mobileapp.client

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.example.cc_mobileapp.Constant.NODE_CLIENT
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import java.util.regex.Pattern

class AddClientDialogFragment : Fragment() {

    // variable
    private lateinit var viewModel: ClientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // provide this class with client view model information
        viewModel = ViewModelProvider(this@AddClientDialogFragment).get(ClientViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_client_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState:Bundle?){
        super.onActivityCreated(savedInstanceState)

        // observe if any changes on the view model result variable
        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it == null) {// success
                getString(R.string.client_added)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message,Toast.LENGTH_SHORT).show()
        })

        // when "+" button is clicked
        btn_addClient.setOnClickListener{
            val clientCoName = edit_text_clientCoName.text.toString().trim()
            val clientEmail = edit_text_clientEmail.text.toString().trim()
            val clientHp = edit_text_clientHp.text.toString().trim()
            val clientLocation = edit_text_clientLocation.text.toString().trim()

            // validation on input data
            var valid: Boolean = true
            // client company name validation
            if(clientCoName.isEmpty()){
                input_layout_clientCoName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_clientCoName.error = null
            }

            // client email address validation
            if(clientEmail.isEmpty()){
                input_layout_clientEmail.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(clientEmail).matches()){
                input_layout_clientEmail.error = getString(R.string.email_format_error)
                return@setOnClickListener
            }
            else{
                input_layout_clientEmail.error = null
            }

            // client contact number validation
            if(clientHp.isEmpty()){
                input_layout_clientHp.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if(!checkRegexhpNum(clientHp)){
                input_layout_clientHp.error = getString(R.string.phone_format_error)
                return@setOnClickListener
            }
            else{
                input_layout_clientHp.error = null
            }

            // client company's location validation
            if(clientLocation.isEmpty()){
                input_layout_clientLocation.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_clientLocation.error = null
            }

            // if the first validation pass, it wil carry out second validation (mostly availability
            if(valid){
                // check duplication of client company name
                var clientNameQuery: Query = FirebaseDatabase.getInstance().reference.child(NODE_CLIENT).orderByChild("clientCoName").equalTo(clientCoName)
                clientNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.childrenCount > 0) {
                            input_layout_clientCoName.error = getString(R.string.exist_clientname_error)
                        } else {
                            // construct client
                            val client = Client()
                            client.clientCoName = clientCoName
                            client.clientEmail = clientEmail
                            client.clientHpNum = clientHp
                            client.clientLocation = clientLocation
                            // add client
                            viewModel.addClient(client)
                            Toast.makeText(requireContext(), "Successfully Add Client", Toast.LENGTH_SHORT).show()
                            // pop back to previous fragment
                            requireActivity().supportFragmentManager.popBackStack("addClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        // as "cancel" button is click, user will pop back to previous fragment
        btn_clientAddBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack("addClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    // phone number validation
    private fun checkRegexhpNum(hpNum: String): Boolean {
        var hpNum: String = hpNum
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = hpNum) && hpNum.startsWith("01")
    }

}


