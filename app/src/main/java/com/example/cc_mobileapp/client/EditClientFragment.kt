package com.example.cc_mobileapp.client

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
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_client.*


class EditClientFragment(private val client: Client) : Fragment() {

    // variable
    private lateinit var  viewModel: ClientViewModel
    private lateinit var oriClientName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // provide this class with client view model information
        viewModel = ViewModelProvider(this@EditClientFragment).get(ClientViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_client, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // set the information from the clicked recycler item to the autocomplete input text
        edit_text_editClientCoName.setText(client.clientCoName)
        edit_text_editClientEmail.setText(client.clientEmail)
        edit_text_editClientHp.setText(client.clientHpNum)
        edit_text_editClientLocation.setText(client.clientLocation)

        // observe if any changes on the view model result variable
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message:String
            if (it == null) {
                message = getString(R.string.client_delete_success)
            } else {
                message = getString(R.string.error, it.message)
            }
            //after changes has been made, a toast of the status will be on text
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show().toString()
            // go back to previous fragment
            requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        btn_clientConfirmEdit.setOnClickListener {
            // retrieve and update client information from input text
            val clientCoName = edit_text_editClientCoName.text.toString().trim()
            val clientEmail = edit_text_editClientEmail.text.toString().trim()
            val clientHp = edit_text_editClientHp.text.toString().trim()
            val clientLocation = edit_text_editClientLocation.text.toString().trim()
            var valid: Boolean = true

            // validation
            // validate client company name
            if(clientCoName.isEmpty()){
                input_layout_editClientCoName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editClientCoName.error = null
            }

            // validate email
            if(clientEmail.isEmpty()){
                input_layout_editClientEmail.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(clientEmail).matches()){
                input_layout_editClientEmail.error = getString(R.string.email_format_error)
                return@setOnClickListener
            }
            else{
                input_layout_editClientEmail.error = null
            }

            // vaidate client contact number
            if(clientHp.isEmpty()){
                input_layout_editClientHp.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if(!checkRegexhpNum(clientHp)){
                input_layout_editClientHp.error = getString(R.string.phone_format_error)
                return@setOnClickListener
            }
            else{
                input_layout_editClientHp.error = null
            }

            // validate client company location
            if(clientLocation.isEmpty()){
                input_layout_editClientLocation.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editClientLocation.error = null
            }

            if(valid) {
                //construct new client with entered information
                val newClient = Client()
                newClient.clientId = client.clientId
                newClient.clientCoName = clientCoName
                newClient.clientEmail = clientEmail
                newClient.clientHpNum = clientHp
                newClient.clientLocation = clientLocation

                // check the existance of the company name
                var clientNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_CLIENT).orderByChild("clientCoName").equalTo(clientCoName)
                clientNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.childrenCount > 0) {
                            if(client.clientCoName != clientCoName){
                                input_layout_editClientCoName.error = getString(R.string.exist_clientname_error)
                            }else{

                                // check if all the text has been changed
                                if(client.clientEmail != newClient.clientEmail || client.clientHpNum != newClient.clientHpNum || client.clientLocation != newClient.clientLocation ){
                                    viewModel.updateClient(newClient)
                                    Toast.makeText(requireContext(), getString(R.string.clientInfo_success), Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(requireContext(), getString(R.string.client_info_remain), Toast.LENGTH_SHORT).show()
                                }
                                requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            }
                        } else {
                            // update client information
                            viewModel.updateClient(newClient)
                            Toast.makeText(requireContext(), getString(R.string.clientInfo_success), Toast.LENGTH_SHORT).show()
                            // go back to revious fragment
                            requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        // when delete button ic clicked the alert dialog will show the infomation
        btn_dialog_clientDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteClient(client)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
            }.create().show()
        }

        // when the cancel button is clicked, go back to previous looks like
        btn_clientEditCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    // check phone number availability
    private fun checkRegexhpNum(hpNum: String): Boolean {
        var hpNum: String = hpNum
        var regex:Regex = Regex(pattern="""\d+""")
        return regex.matches(input = hpNum) && hpNum.startsWith("01")
    }
}
