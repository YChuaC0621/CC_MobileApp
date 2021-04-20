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
import com.example.cc_mobileapp.rack.RackViewModel
import com.example.cc_mobileapp.supplier.AddSupplierDialogFragment
import com.example.cc_mobileapp.supplier.SupplierViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*
import kotlinx.android.synthetic.main.fragment_sitemap.*
import kotlinx.android.synthetic.main.fragment_supplier.*
import kotlinx.android.synthetic.main.fragment_supplier.button_add
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SiteMapFragment: Fragment() {
    //data declaration
    private lateinit var viewModel: RackViewModel
    private val dbStock = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)
    private val dbProduct = FirebaseDatabase.getInstance().getReference(Constant.NODE_PRODUCT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //bind view model to view
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@SiteMapFragment).get(RackViewModel::class.java)

        return inflater.inflate(R.layout.fragment_sitemap, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Check", "supplier fragment on activity created")

        super.onActivityCreated(savedInstanceState)

        //call the add rack process
        button_add.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddRackDialogFragment())
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        //call the display rack process
        btn_rack1.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack1.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack2.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack2.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack3.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack3.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack4.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack4.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack5.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack5.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack6.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack6.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack7.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack7.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack8.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack8.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack9.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack9.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack10.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack10.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack11.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack11.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack12.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack12.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack13.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack13.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack14.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack14.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack15.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack15.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack16.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack16.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack17.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack17.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack18.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack18.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack19.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack19.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        btn_rack20.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, DisplayRackDetailsFragment(btn_rack20.text.toString()))
            transaction.addToBackStack("fragmentA")
            transaction.commit()
        }

        //call the searching product process
        button_search.setOnClickListener {

            //validation to prevent null pointer exception
            if (!search_editText.text.equals("")) {
                //coroutine
                GlobalScope.launch(Dispatchers.IO) {
                    searchResult()
                }

            }


        }
    }

    suspend fun searchResult() {
        //retreive product data from product database
        dbProduct.get().addOnSuccessListener {
            var counter = 0
            var prodcounter = 0
            if (it.exists()) {
                var result = "Searching Results : \n"

                for (prod in it.children) {
                    var product: Product? =
                        prod.getValue(Product::class.java)

                    //check the product is the one that user request or not
                    //if yes find its stock details transaction
                    if (product?.prodName!!.equals(search_editText.text.toString())) {
                        val prodId = product?.prodBarcode.toString()
                        prodcounter = 1
                        dbStock.get().addOnSuccessListener {
                            Log.d("Check", "fetch stock details")
                            if (it.exists()) {
                                it.children.forEach { it ->
                                    val stock: StockDetail? = it.getValue(StockDetail::class.java)

                                    //check this product is stored at which rack and the dtored quantity
                                    if (stock?.stockDetailProdBarcode == prodId) {
                                        result += (stock?.stockDetailRackId.toString() + " : " + stock?.stockDetailQty.toString() + "Quantity \n")
                                        counter = 1
                                        search_results.setText(result)
                                    }

                                }
                                if(counter == 0)
                                {
                                    Toast.makeText(requireContext(), getString(R.string.search_not_found), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }

                }
                if(prodcounter == 0 )
                {
                    Toast.makeText(requireContext(), getString(R.string.search_not_found), Toast.LENGTH_SHORT).show()
                }

            }

        }


    }
}