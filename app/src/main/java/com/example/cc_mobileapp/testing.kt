package com.example.cc_mobileapp

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity

class testing : AppCompatActivity(){
    private lateinit var listViewAdapter: ExpandableListViewAdapter
    private lateinit var chapterList : List<String>
    private lateinit var topicsList : HashMap<String,List<String>>
    private lateinit var myListView : ExpandableListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testing)

        showList()

        listViewAdapter = ExpandableListViewAdapter(this,chapterList,topicsList)
        myListView = findViewById(R.id.eListView) as ExpandableListView
        myListView.setAdapter(listViewAdapter)

    }

    private fun showList() {
        chapterList = ArrayList()
        topicsList = HashMap()

        (chapterList as ArrayList<String>).add("Chapter 1")
        (chapterList as ArrayList<String>).add("Chapter 2")
        (chapterList as ArrayList<String>).add("Chapter 3")
        (chapterList as ArrayList<String>).add("Chapter 4")
        (chapterList as ArrayList<String>).add("Chapter 5")

        val topic1 : MutableList<String> = ArrayList()
        topic1.add("topics 1")
        topic1.add("topics 2")
        topic1.add("topics 3")

        val topic2 : MutableList<String> = ArrayList()
        topic2.add("topics 1")
        topic2.add("topics 2")
        topic2.add("topics 3")

        val topic3 : MutableList<String> = ArrayList()
        topic3.add("topics 1")
        topic3.add("topics 2")
        topic3.add("topics 3")

        val topic4 : MutableList<String> = ArrayList()
        topic4.add("topics 1")
        topic4.add("topics 2")
        topic4.add("topics 3")

        val topic5 : MutableList<String> = ArrayList()
        topic5.add("topics 1")
        topic5.add("topics 2")
        topic5.add("topics 3")

        topicsList[chapterList[0]] = topic1
        topicsList[chapterList[1]] = topic2
        topicsList[chapterList[2]] = topic3
        topicsList[chapterList[3]] = topic4
        topicsList[chapterList[4]] = topic5

    }

}