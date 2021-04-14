package com.example.cc_mobileapp.rack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.Rack
import com.google.firebase.database.FirebaseDatabase
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.fragment_add_rack.*
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*
import kotlinx.android.synthetic.main.fragment_sitemap.*
import kotlinx.coroutines.*
import java.io.IOException

class DisplayRackDetailsFragment(private val rack_num : String) : Fragment() {
    private lateinit var viewModel: RackViewModel
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private var counterRack = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel =
                ViewModelProvider(this@DisplayRackDetailsFragment).get(RackViewModel::class.java)
        return inflater.inflate(R.layout.fragment_display_rackdetails, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            checkRack()
        }
    }

    suspend fun checkRack() {
        dbRack.get().addOnSuccessListener {
            Log.d("Check", "fetch racks")
            if (it.exists()) {
                it.children.forEach { it ->
                    val rack: Rack? = it.getValue(Rack::class.java)
                    if (rack?.rackNum == rack_num) {
                        rack?.rackId = it.key
                        txtRackNum.setText(rack?.rackNum.toString())
                        txtStartLot.setText(rack?.startLot.toString())
                        txtEndLot.setText(rack?.endLot.toString())
                        txtRowNum.setText("2")
                        counterRack = 1
                        if (rack?.row_num == 1) {
                            if (rack?.prodId.equals("0")) {
                                txtRow1Prod.text = null
                                txtRow1ProdQty.text = null
                                Toast.makeText(
                                        requireContext(),
                                        "No product is stored under this " + rack?.rackName,
                                        Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                dbProduct.get().addOnSuccessListener {
                                    if (it.exists()) {
                                        for (prod in it.children) {
                                            var product: Product? =
                                                    prod.getValue(Product::class.java)
                                            if (product?.prodBarcode!!.equals(rack?.prodId)) {
                                                txtRow1Prod.setText(product?.prodName.toString())
                                                txtRow1ProdQty.setText(rack?.currentQty.toString())
                                                Log.d(
                                                        "Check",
                                                        "fetch product ${product?.prodName.toString()}"
                                                )
                                                Log.d(
                                                        "Check",
                                                        "fetch product ${rack?.currentQty.toString()}"
                                                )

                                            }
                                        }
                                    }
                                }

                            }

                        } else {
                            if (rack?.prodId.equals("0")) {
                                txtRow2Prod.text = null
                                txtRow2ProdQty.text = null
                                Toast.makeText(
                                        requireContext(),
                                        "No product is stored under this " + rack?.rackName,
                                        Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                dbProduct.get().addOnSuccessListener {
                                    if (it.exists()) {
                                        for (prod in it.children) {
                                            var product: Product? =
                                                    prod.getValue(Product::class.java)
                                            if (product?.prodBarcode!!.equals(rack?.prodId)) {
                                                txtRow2Prod.setText(product?.prodName.toString())
                                                txtRow2ProdQty.setText(rack?.currentQty.toString())
                                                Log.d(
                                                        "Check",
                                                        "fetch product ${product?.prodName.toString()}"
                                                )
                                                Log.d(
                                                        "Check",
                                                        "fetch product ${rack?.currentQty.toString()}"
                                                )

                                            }
                                        }
                                    }
                                }
                            }


                        }
                    }
                }
            }

            if (counterRack == 0) {
                Toast.makeText(
                        requireContext(),
                        "No information is placed under this " + rack_num,
                        Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}

    /*lifecycleScope.launch {
        withContext(Dispatchers.IO) {
            // Small delay so the user can actually see the splash screen
            // for a moment as feedback of an attempt to retrieve data.
            delay(250)
            try {
                checkRack()
                CoroutineScope(Dispatchers.Main).launch {
                    if (counterRack == 0) {
                        Toast.makeText(
                                requireContext(),
                                "No information is placed under this " + rack_num,
                                Toast.LENGTH_SHORT
                        ).show()

                    }
                    else
                    {
                        Log.d(
                                "Check",
                                "done ${rack_num}"
                        )
                    }

                }
            }catch (e: Error)
            {
                Log.d(
                        "Check",
                        "fetch error message ${e.message}"
                )
            }
        }
    }*/

