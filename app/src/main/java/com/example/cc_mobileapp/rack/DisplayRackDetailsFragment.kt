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
import com.example.cc_mobileapp.model.StockDetail
import com.google.firebase.database.FirebaseDatabase
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.fragment_add_rack.*
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*
import kotlinx.android.synthetic.main.fragment_sitemap.*
import kotlinx.coroutines.*
import java.io.IOException

//recv the rack number that user request to display its info
class DisplayRackDetailsFragment(private val rack_num : String) : Fragment() {
    //data declaration
    private lateinit var viewModel: RackViewModel
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)
    private val dbStock = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private var counterRack = 0

    //link view model to view
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

        //coroutine to let the data retrieval run in background
        GlobalScope.launch(Dispatchers.IO) {
            checkRack()
        }
    }

    suspend fun checkRack() {
        //access rack database
        dbRack.get().addOnSuccessListener {
            Log.d("Check", "fetch racks")
            if (it.exists()) {
                it.children.forEach { it ->
                    val rack: Rack? = it.getValue(Rack::class.java)

                    //check the rack number is it same with the rack that user request
                    //if same fill up the textfield in view
                    if (rack?.rackNum == rack_num) {
                        rack?.rackId = it.key
                        txtRackNum.setText(rack?.rackNum.toString())
                        txtStartLot.setText(rack?.startLot.toString())
                        txtEndLot.setText(rack?.endLot.toString())
                        txtRowNum.setText("2")
                        counterRack = 1

                        //check that this rack record is for which row number
                        //if match the row number find relative information in stock detail database
                        if (rack?.row_num == 1) {
                            dbStock.get().addOnSuccessListener {
                                if (it.exists()) {
                                    for (stocks in it.children) {
                                        var stock: StockDetail? =
                                                stocks.getValue(StockDetail::class.java)

                                        //check the stock details transaction is related or not
                                        //if yes find product database to retrieve the product name and display
                                        if (stock?.stockDetailRackId!!.equals(rack?.rackName)) {
                                            dbProduct.get().addOnSuccessListener {
                                                if (it.exists()) {
                                                    for (prod in it.children) {
                                                        var product: Product? =
                                                                prod.getValue(Product::class.java)

                                                        //check the product barcode is stored in this respective stock detail transaction or not
                                                        //If yes set the text field with information
                                                        if (product?.prodBarcode!!.equals(stock?.stockDetailProdBarcode)) {
                                                            txtRow1Prod.setText(product?.prodName.toString())
                                                            txtRow1ProdQty.setText(stock?.stockDetailQty.toString())
                                                            rack?.prodId = stock?.stockDetailProdBarcode
                                                            rack?.currentQty = stock?.stockDetailQty
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
                                        //if no relevant stock details transaction mean there is no product is stored in this rack
                                        else
                                        {
                                            txtRow1Prod.text = null
                                            txtRow1ProdQty.text = null
                                            rack?.prodId = "0"
                                            rack?.currentQty = 0
                                        }
                                    }
                                }
                            }

                        }
                        //same process for row 2
                        else {
                            dbStock.get().addOnSuccessListener {
                                if (it.exists()) {
                                    for (stocks in it.children) {
                                        var stock: StockDetail? =
                                                stocks.getValue(StockDetail::class.java)
                                        if (stock?.stockDetailRackId!!.equals(rack?.rackName)) {
                                            dbProduct.get().addOnSuccessListener {
                                                if (it.exists()) {
                                                    for (prod in it.children) {
                                                        var product: Product? =
                                                                prod.getValue(Product::class.java)
                                                        if (product?.prodBarcode!!.equals(stock?.stockDetailProdBarcode)) {
                                                            txtRow2Prod.setText(product?.prodName.toString())
                                                            txtRow2ProdQty.setText(stock?.stockDetailQty.toString())
                                                            rack?.prodId = stock?.stockDetailProdBarcode
                                                            rack?.currentQty = stock?.stockDetailQty
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
                                        } else {
                                            txtRow2Prod.text = null
                                            txtRow2ProdQty.text = null
                                            rack?.prodId = "0"
                                            rack?.currentQty = 0
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //if the rack still no yet be added mean unavailable
            if (counterRack == 0) {
                Toast.makeText(
                        requireContext(),
                        getString(R.string.NoResults) + rack_num,
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

