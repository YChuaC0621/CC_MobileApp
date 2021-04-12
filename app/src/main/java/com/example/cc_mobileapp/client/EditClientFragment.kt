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

    private lateinit var  viewModel: ClientViewModel
    private lateinit var oriClientName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@EditClientFragment).get(ClientViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_client, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_text_editClientCoName.setText(client.clientCoName)
        edit_text_editClientEmail.setText(client.clientEmail)
        edit_text_editClientHp.setText(client.clientHpNum)
        edit_text_editClientLocation.setText(client.clientLocation)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message:String
            if (it == null) {
                message = (R.string.client_added).toString()
            } else {
                message = getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show().toString()
            requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //dismiss()
        })

        btn_clientConfirmEdit.setOnClickListener {
            val clientCoName = edit_text_editClientCoName.text.toString().trim()
            val clientEmail = edit_text_editClientEmail.text.toString().trim()
            val clientHp = edit_text_editClientHp.text.toString().trim()
            val clientLocation = edit_text_editClientLocation.text.toString().trim()
            var valid: Boolean = true

            if(clientCoName.isEmpty()){
                input_layout_editClientCoName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editClientCoName.error = null
            }

            if(clientEmail.isEmpty()){
                input_layout_editClientEmail.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(clientEmail).matches()){
                input_layout_editClientEmail.error = "Invalid Input"
                return@setOnClickListener
            }
            else{
                input_layout_editClientEmail.error = null
            }

            if(clientHp.isEmpty()){
                input_layout_editClientHp.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else if (!android.util.Patterns.PHONE.matcher(clientHp).matches()){
                input_layout_editClientHp.error = "Invalid Input"
                return@setOnClickListener
            }
            else{
                input_layout_editClientHp.error = null
            }

            if(clientLocation.isEmpty()){
                input_layout_editClientLocation.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            }
            else{
                input_layout_editClientLocation.error = null
            }

            if(valid) {
                val newClient = Client()
                newClient.clientId = client.clientId
                newClient.clientCoName = clientCoName
                newClient.clientEmail = clientEmail
                newClient.clientHpNum = clientHp
                newClient.clientLocation = clientLocation
                var clientNameQuery: Query = FirebaseDatabase.getInstance().reference.child(Constant.NODE_CLIENT).orderByChild("clientCoName").equalTo(clientCoName)
                clientNameQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.childrenCount > 0) {
                            if(client.clientCoName != clientCoName){
                                Toast.makeText(requireActivity(), "Client Company Name Exist", Toast.LENGTH_SHORT).show()
                                input_layout_editClientCoName.error = "Client Company Name Exist"
                            }else{
                                if(client.clientEmail != newClient.clientEmail || client.clientHpNum != newClient.clientHpNum || client.clientLocation != newClient.clientLocation ){
                                    viewModel.updateClient(newClient)
                                }else{
                                    Toast.makeText(requireContext(), "Client information remain unchanged", Toast.LENGTH_SHORT).show()
                                }
                                requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            }
                        } else {
                            viewModel.updateClient(newClient)
                            requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        btn_dialog_clientDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteClient(client)
                }
                it.setNegativeButton("No"){dialog, which -> dialog.dismiss()}
            }.create().show()
        }

        btn_clientEditBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack("editClientFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}
