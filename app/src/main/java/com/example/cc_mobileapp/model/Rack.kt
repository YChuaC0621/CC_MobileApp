package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude

public class Rack (
    @get:Exclude
    var rackId: String? = null,

    //rackNum + rowNum = rackName
    var rackName: String? = null,
    var rackNum: String? = null,
    var startLot: String? = null,
    var endLot: String? = null,
    var row_num: Int? = null,
    var prodId: Int? = null,
    var currentQty: Int? = null
)
{

}