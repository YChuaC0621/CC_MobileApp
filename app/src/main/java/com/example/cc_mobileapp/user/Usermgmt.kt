package com.example.cc_mobileapp.user

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.R

class Usermgmt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermgmt_display)

        val list: MutableList<String> = ArrayList()

        list.add("Bright Theme");
        list.add("Dark Theme");

        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        val ddlTheme:Spinner = findViewById(R.id.ddlTheme)
        ddlTheme.adapter = adapter
        ddlTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = list[position]
                Toast.makeText(this@Usermgmt, "$item selected",Toast.LENGTH_SHORT).show()
            }
        }
        limitDdlLength(ddlTheme)

    }

    fun limitDdlLength(ddlTheme:Spinner)
    {
        val popup = Spinner::class.java.getDeclaredField("Popup")
        popup.isAccessible = true

        val popupWindow = popup.get(ddlTheme) as ListPopupWindow
        popupWindow.height = (200 * resources.displayMetrics.density).toInt()
    }
}