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
import com.example.cc_mobileapp.model.StockDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_rack.*
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*
import kotlinx.android.synthetic.main.fragment_sitemap.*

class DisplayRackDetailsFragment(private val rack_name : String) : Fragment() {
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
        var counterRow1 = 0
        var counterRow2 = 0
        var counterRow3 = 0
        dbRack.get().addOnSuccessListener {
            Log.d("Check", "fetch racks")
            if (it.exists()) {
                it.children.forEach { it ->
                    val rack: Rack? = it.getValue(Rack::class.java)
                    if (rack?.rackName == rack_name) {
                        rack?.rackId = it.key
                        txtRackName.setText(rack?.rackName.toString())
                        txtStartLot.setText(rack?.startLot.toString())
                        txtEndLot.setText(rack?.endLot.toString())
                        txtRowNum.setText(rack?.row_num.toString())

                        dbStockDetail.get().addOnSuccessListener {
                            Log.d("Check", "fetch stock details")
                            if (it.exists()) {
                                for(sd in it.children){
                                    val stockDetail :StockDetail? = sd.getValue(StockDetail::class.java)
                                    if (stockDetail?.stockDetailRackId == rack.rackName && stockDetail?.stockDetailRowNum == "1") {
                                        Log.d("Check", "fetch product row 1")
                                        dbProduct.get().addOnSuccessListener {
                                            if (it.exists()) {
                                                for(prod in it.children){
                                                    var product: Product? = prod.getValue(Product::class.java)
                                                    if (product?.prodBarcode.toString() == stockDetail?.stockDetailProdBarcode) {
                                                        txtRow1Prod.setText(product?.prodName.toString())
                                                        txtRow1ProdQty.setText(product?.prodQty.toString())
                                                        Log.d("Check", "fetch product ${product?.prodName.toString()}")
                                                        Log.d("Check", "fetch product ${product?.prodQty.toString()}")
                                                    }
                                                }
                                            }
                                        }
                                    } else if (stockDetail?.stockDetailRackId == rack.rackName && stockDetail?.stockDetailRowNum == "2") {
                                        Log.d("Check", "fetch product row 2")
                                        dbProduct.get().addOnSuccessListener {
                                            if (it.exists()) {
                                                it.children.forEach {
                                                    var product: Product? = it.getValue(Product::class.java)
                                                    if (product?.prodBarcode.toString() == stockDetail?.stockDetailProdBarcode) {
                                                        txtRow2Prod.setText(product?.prodName.toString())
                                                        txtRow2ProdQty.setText(product?.prodQty.toString())
                                                        Log.d("Check", "fetch product ${product?.prodName.toString()}")
                                                        Log.d("Check", "fetch product ${product?.prodQty.toString()}")
                                                    }
                                                }
                                            }
                                        }
                                    } else if (stockDetail?.stockDetailRackId == rack.rackName && stockDetail?.stockDetailRowNum == "3") {
                                        Log.d("Check", "fetch product row 3")
                                        dbProduct.get().addOnSuccessListener {
                                            if (it.exists()) {
                                                it.children.forEach { it ->
                                                    var product: Product? = it.getValue(Product::class.java)
                                                    if (product?.prodBarcode.toString() == stockDetail?.stockDetailProdBarcode) {
                                                        txtRow3Prod.setText(product?.prodName.toString())
                                                        txtRow3ProdQty.setText(product?.prodQty.toString())
                                                        Log.d("Check", "fetch product ${product?.prodName.toString()}")
                                                        Log.d("Check", "fetch product ${product?.prodQty.toString()}")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(counterRow1==0 && counterRow2 ==0 && counterRow3 ==0)
                                {
                                    Toast.makeText(requireContext(), "No product is placed under this " + rack_name, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                }

            }


        }

    }
}
