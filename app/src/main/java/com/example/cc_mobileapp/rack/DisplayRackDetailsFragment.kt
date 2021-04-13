package com.example.cc_mobileapp.rack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.Rack
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_rack.*
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*
import kotlinx.android.synthetic.main.fragment_sitemap.*

class DisplayRackDetailsFragment(private val rack_num : String) : Fragment() {
    private lateinit var viewModel: RackViewModel
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)
    private val dbStockDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)

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

        var counterRack = 0
        var counterRow = 0
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
                        if(rack?.row_num == 1)
                        {

                        }
                        else
                        {
                            dbProduct.get().addOnSuccessListener {
                                if (it.exists()) {
                                    for(prod in it.children){
                                        var product: Product? = prod.getValue(Product::class.java)
                                        if (product?.prodBarcode == rack?.prodId) {
                                            txtRow1Prod.setText(product?.prodName.toString())
                                            txtRow1ProdQty.setText(rack?.currentQty.toString())
                                            Log.d("Check", "fetch product ${product?.prodName.toString()}")
                                            Log.d("Check", "fetch product ${rack?.currentQty.toString()}")

                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        if(counterRack == 0)
        {
            txtRackNum.text = null
            txtStartLot.text = null
            txtEndLot.text = null
            txtRowNum.text = null
            txtRow1Prod.text = null
            txtRow1ProdQty.text = null
            txtRow2Prod.text = null
            txtRow2ProdQty.text = null
            Toast.makeText(requireContext(), "No information is placed under this " + rack_num, Toast.LENGTH_SHORT).show()
        }

    }
}
