package com.example.cc_mobileapp.product

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_client.*
import kotlinx.android.synthetic.main.fragment_edit_product.*

class EditProductFragment(private val product: Product) : Fragment() {

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@EditProductFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_text_editProductName.setText(product.prodName)
        edit_text_editSupplierId.setText(product.supplierId)
        edit_text_editProdDesc.setText(product.prodDesc)
        edit_text_editProdPrice.setText(product.prodPrice.toString())
        edit_text_editProdBarcode.setText(product.prodBarcode.toString())

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                "Product edit successfully"
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            //dismiss()
            requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        btn_productConfirmEdit.setOnClickListener {
            val prodName = edit_text_editProductName.text.toString().trim()
            val supplierId = edit_text_editSupplierId.text.toString().trim()
            val prodDesc = edit_text_editProdDesc.text.toString().trim()
            val prodPrice = edit_text_editProdPrice.text.toString().trim()
            val prodBarcode = edit_text_editProdBarcode.text.toString().trim()
            when {
                prodName.isEmpty() -> {
                    input_layout_clientCoName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                supplierId.isEmpty() -> {
                    input_layout_clientEmail.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodDesc.isEmpty() -> {
                    input_layout_clientHp.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodPrice.isEmpty() -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodBarcode.isEmpty() -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    product.prodName = prodName
                    product.supplierId = supplierId
                    product.prodDesc = prodDesc
                    product.prodPrice = prodPrice.toFloat()
                    product.prodBarcode = prodBarcode.toInt()
                    Log.d("Check", "Update client data $product")
                    viewModel.updateProduct(product)
                }
            }
        }

        btn_productDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteProduct(product)
                }
            }.create().show()
        }
    }

}