package com.example.cc_mobileapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ExpandableListView.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class supplier : AppCompatActivity() {

    class supplierTitle(
        private val supplier_name: String,
        private val cmp_name: String
    )
    {
        fun getSupName():String {return supplier_name}
        fun getCmpName():String {return cmp_name}
    }

    class stockInInfo(
        private val prodName: String,
        private val stockInDate: Date,
        private val stock_status: Int
    )
    {
        fun getProdName():String {return prodName}
        fun getStockInDate():Date {return stockInDate}
        fun getStockStatus():Int {return stock_status}
    }

    class CustomAdapter(
        private var c: Context,
        private var supplierList: List<supplierTitle>,
        private var supInfoList: HashMap<supplierTitle, List<stockInInfo>>
    ) : BaseExpandableListAdapter() {
        override fun getGroupCount(): Int {
            return supplierList.size
        }

        override fun getChildrenCount(groupPosition: Int): Int {
           return this.supInfoList[this.supplierList[groupPosition]]!!.size
        }

        override fun getGroup(groupPosition: Int): supplierTitle {
            return supplierList[groupPosition]
        }

        override fun getChild(groupPosition: Int, childPosition: Int): Any {
            return this.supInfoList[this.supplierList[groupPosition]]!![childPosition]
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        override fun getGroupView(
            groupPosition: Int,
            isExpanded: Boolean,
            convertView: View?,
            parent: ViewGroup?
        ): View {
            var convertView = convertView
            val sup = getGroup(groupPosition) as supplierTitle
            if(convertView == null)
            {
                val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.customize_listview, null)
            }

            //reference textviews from our layout
            val txtSupName = convertView!!.findViewById<TextView>(R.id.txtSupplierName)
            val txtCmpName = convertView!!.findViewById<TextView>(R.id.txtCmpName)

            //Bind data to textview
            txtSupName.text = sup.getSupName()
            txtCmpName.text = sup.getCmpName()

            //handle itemclicks for the ListView
            convertView.setOnClickListener {
                Toast.makeText(c, sup.getSupName(), Toast.LENGTH_SHORT).show()

            }

            return convertView
        }

        override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup?
        ): View {
            var convertView = convertView
            val sup = getChild(groupPosition, childPosition) as stockInInfo
            if(convertView == null)
            {
                val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.customize_childlistview, null)
            }

            //reference textviews from our layout
            val txtProdName = convertView!!.findViewById<TextView>(R.id.txtTesting1)
            val txtStockInDate = convertView!!.findViewById<TextView>(R.id.txtTesting2)
            val txtStockStatus = convertView!!.findViewById<TextView>(R.id.txtTesting3)

            //Bind data to textview
            txtProdName.text = sup.getProdName()
            txtStockInDate.text = sup.getStockInDate().toString()
            txtStockStatus.text = sup.getStockStatus().toString()

            //handle itemclicks for the ListView
            //convertView.setOnClickListener {
             //   Toast.makeText(c, sup.getProdName(), Toast.LENGTH_SHORT).show()
            //}

            return convertView
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return true
        }

    }


    //Supplier Instance Fields.
    private lateinit var listViewAdapter: CustomAdapter
    private lateinit var supplierList : List<supplierTitle>
    private lateinit var stockInList : HashMap<supplierTitle, List<stockInInfo>>
    private lateinit var myListView : ExpandableListView

    /*
    When activity is created, reference ListView and set its adapter
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier)

        showList()

        listViewAdapter = CustomAdapter(this, supplierList, stockInList)
        myListView = findViewById(R.id.lv_supplier) as ExpandableListView
        myListView.setAdapter(listViewAdapter)

    }

    private fun showList() {
        supplierList = ArrayList()
        stockInList = HashMap()

        var sup = supplierTitle("AAA", "Company AAA")
        (supplierList as ArrayList<supplierTitle>).add(sup)
         sup = supplierTitle("BBB", "Company BBB")
        (supplierList as ArrayList<supplierTitle>).add(sup)
         sup = supplierTitle("CCC", "Company CCC")
        (supplierList as ArrayList<supplierTitle>).add(sup)
        sup = supplierTitle("DDD", "Company DDD")
        (supplierList as ArrayList<supplierTitle>).add(sup)
        sup = supplierTitle("EEE", "Company EEE")
        (supplierList as ArrayList<supplierTitle>).add(sup)

        val stockIn1 : ArrayList<stockInInfo> = ArrayList()
        var stockInInfo = stockInInfo("Orange", Date(2020, 1, 10), 5)
        stockIn1.add(stockInInfo)
        stockInInfo = stockInInfo("Apple", Date(2020, 2, 15), 2)
        stockIn1.add(stockInInfo)
        stockInInfo = stockInInfo("Grape", Date(2020, 3, 20), 1)
        stockIn1.add(stockInInfo)

        val stockIn2 : ArrayList<stockInInfo> = ArrayList()
         stockInInfo = stockInInfo("Television", Date(2020, 1, 10), 5)
        stockIn2.add(stockInInfo)
        stockInInfo = stockInInfo("Refrigerator", Date(2020, 2, 15), 2)
        stockIn2.add(stockInInfo)
        stockInInfo = stockInInfo("Radio", Date(2020, 3, 20), 1)
        stockIn2.add(stockInInfo)

        val stockIn3 : ArrayList<stockInInfo> = ArrayList()
         stockInInfo = stockInInfo("Carrot", Date(2020, 1, 10), 5)
        stockIn3.add(stockInInfo)
        stockInInfo = stockInInfo("Cabbage", Date(2020, 2, 15), 2)
        stockIn3.add(stockInInfo)
        stockInInfo = stockInInfo("Broccoli", Date(2020, 3, 20), 1)
        stockIn3.add(stockInInfo)

        val stockIn4 : ArrayList<stockInInfo> = ArrayList()
         stockInInfo = stockInInfo("Toilet Paper", Date(2020, 1, 10), 5)
        stockIn4.add(stockInInfo)
        stockInInfo = stockInInfo("Toilet Seat", Date(2020, 2, 15), 2)
        stockIn4.add(stockInInfo)
        stockInInfo = stockInInfo("Bathtub", Date(2020, 3, 20), 1)
        stockIn4.add(stockInInfo)

        val stockIn5 : ArrayList<stockInInfo> = ArrayList()
         stockInInfo = stockInInfo("Pencil box", Date(2020, 1, 10), 5)
        stockIn5.add(stockInInfo)
        stockInInfo = stockInInfo("Envelop", Date(2020, 2, 15), 2)
        stockIn5.add(stockInInfo)
        stockInInfo = stockInInfo("Pencil Sharpener", Date(2020, 3, 20), 1)
        stockIn5.add(stockInInfo)

        stockInList[supplierList[0]] = stockIn1
        stockInList[supplierList[1]] = stockIn2
        stockInList[supplierList[2]] = stockIn3
        stockInList[supplierList[3]] = stockIn4
        stockInList[supplierList[4]] = stockIn5


    }


}