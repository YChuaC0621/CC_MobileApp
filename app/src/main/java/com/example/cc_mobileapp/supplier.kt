package com.example.cc_mobileapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class supplier : AppCompatActivity() {

    class supplierCollection(
        private val supplier_name:String,
        private val cmp_name:String,
        private val supplier_hpNum:String,
        private val supplier_email:String,
        private val cmp_location:String,
        private val service_status:Int)
    {
        fun getSupName():String {return supplier_name}
        fun getCmpName():String {return cmp_name}
        fun getSupHpNum():String {return supplier_hpNum}
        fun getSupEmail():String {return supplier_email}
        fun getCmpLot():String {return cmp_location}
        fun getServiceStatus():Int {return service_status}
    }

    class CustomAdapter(private var c: Context, private var supplierList: ArrayList<supplierCollection>) : BaseAdapter() {
        override fun getCount(): Int {
            return supplierList.size
        }

        override fun getItem(i: Int): Any {
            return supplierList[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var view = view
            if (view == null) {
                //inflate layout resource if its null
                view = LayoutInflater.from(c).inflate(R.layout.customize_listview, viewGroup, false)
            }

            //get current supplier item
            val sup = this.getItem(i) as supplierCollection

            //reference textviews from our layout
            val txtSupName = view?.findViewById<TextView>(R.id.txtView_productDesc) as TextView
            val txtCmpName = view?.findViewById<TextView>(R.id.txtCmpName) as TextView

            //Bind data to textview
            txtSupName.text = sup.getSupName()
            txtCmpName.text = sup.getCmpName()

            //handle itemclicks for the ListView
            if (view != null) {
                view.setOnClickListener {
                    Toast.makeText(c, sup.getSupName(), Toast.LENGTH_SHORT).show()
                }
            }

            return view!!
        }
    }


    //Supplier Instance Fields.
    private lateinit var adapter: CustomAdapter
    private lateinit var myListView: ListView
    // our data source
    private val data: ArrayList<supplierCollection>
        get() {
            val supList = ArrayList<supplierCollection>()

            var sup = supplierCollection("AAA", "Company AAA", "010-1234567", "aaa@gmail.com", "1 jalan aaa", 1)
            supList.add(sup)
            sup = supplierCollection("BBB", "Company BBB", "011-1234567", "bbb@gmail.com", "2 jalan bbb", 1)
            supList.add(sup)
            sup = supplierCollection("CCC", "Company CCC", "012-1234567", "ccc@gmail.com", "3 jalan ccc", 1)
            supList.add(sup)
            sup = supplierCollection("DDD", "Company DDD", "013-1234567", "ddd@gmail.com", "4 jalan ddd", 1)
            supList.add(sup)
            sup = supplierCollection("EEE", "Company EEE", "014-1234567", "eee@gmail.com", "5 jalan eee", 1)
            supList.add(sup)

            return supList
        }

    /*
    When activity is created, reference ListView and set its adapter
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier)

        myListView = findViewById(R.id.lv_supplier) as ListView

        //instantiate and set adapter
        adapter = CustomAdapter(this, data)
        myListView.adapter = adapter
    }

}