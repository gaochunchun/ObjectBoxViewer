package com.ccn.objectbox

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.ccn.objectboxviewer.ObjectBoxViewer
import com.ccn.objectboxviewer.ShowDataActivity
import io.objectbox.Box

class MainActivity : AppCompatActivity() {
    var boxManager: Box<BusinessSampleTable> = ObjectBoxManager.get().boxFor(BusinessSampleTable::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        //add
        findViewById<Button>(R.id.addData).setOnClickListener {
            for (i in 1..10) {
                val data = BusinessSampleTable()
                data.orderNo = "20220102$i"
                data.code = "${i}1111111"
                data.productName = "产品名称${i}"
                data.productName2 = "22产品名称${i}"
                data.productName3 = "33产品名称${i}"
                data.productName4 = "44产品名称${i}"
                data.productName5 = "55产品名称${i}"
                data.productName6 = "66产品名称${i}"
                data.productName7 = "77产品名称${i}"
                boxManager.put(data)
                println(data.toString())
            }
        }

        //show
        findViewById<Button>(R.id.showData).setOnClickListener {
            startActivity(Intent(this, ShowDataActivity::class.java))
        }
    }
}