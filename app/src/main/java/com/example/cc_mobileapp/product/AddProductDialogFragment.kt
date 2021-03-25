package com.example.cc_mobileapp.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.Product
import com.google.common.primitives.UnsignedBytes.toInt
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*

class AddProductDialogFragment : Fragment() {

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@AddProductDialogFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_product_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it==null){
                getString(R.string.prodAddSuccess)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        btn_addProd.setOnClickListener {
            val prodName = edit_text_prodName.text.toString().trim()
            val supplierId = edit_text_prodSupplierId.text.toString().trim()
            val prodDesc = edit_text_prodDesc.text.toString().trim()
            val prodPrice = (edit_text_prodPrice.text.toString().trim()).toFloat()
            val prodBarcode = (edit_text_prodBarcode.text.toString().trim()).toInt()
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
                prodPrice.equals("") -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                prodBarcode == 0 -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    val product = Product()
                    product.prodName = prodName
                    product.supplierId = supplierId
                    product.prodDesc = prodDesc
                    product.prodPrice = prodPrice
                    product.prodBarcode = prodBarcode
                    Log.d("Check", "client data $product")
                    viewModel.addProduct(product)
                }
            }
        }
    }
}