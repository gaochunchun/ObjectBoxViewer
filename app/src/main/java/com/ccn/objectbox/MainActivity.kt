package com.ccn.objectbox

import android.content.Intent
import com.ccn.objectbox.databinding.ActivityMainBinding
import com.ccn.objectbox.table.BusinessSampleTable
import com.ccn.objectbox.table.BusinessSampleTable2
import com.ccn.objectbox.table.BusinessSampleTable_
import com.ccn.objectbox.table.ObjectBoxManager
import com.ccn.objectboxviewer.activity.ShowDataActivity
import com.ccn.objectboxviewer.base.BaseBindingActivity
import io.objectbox.Box
import io.objectbox.kotlin.inValues
import io.objectbox.query.QueryBuilder

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    var boxManager: Box<BusinessSampleTable> = ObjectBoxManager.get().boxFor(BusinessSampleTable::class.java)
    var boxManager2: Box<BusinessSampleTable2> = ObjectBoxManager.get().boxFor(BusinessSampleTable2::class.java)


    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initData() {
        //add
        binding.addData.setOnClickListener {
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

            for (i in 1..10) {
                val data = BusinessSampleTable2()
                data.orderNo = "20220102$i"
                data.code = "${i}1111111"
                data.productName = "产品名称${i}"
                boxManager2.put(data)
                println(data.toString())
            }
        }

        //show
        binding.showData.setOnClickListener {
            startActivity(Intent(this, ShowDataActivity::class.java))
        }

        binding.obdata.setOnClickListener {
            startActivity(Intent(this, ShowDataActivity::class.java))
        }
    }

    fun query() {
        boxManager.query().run {
            inValues(BusinessSampleTable_.orderNo, arrayOf("1", "2"), QueryBuilder.StringOrder.CASE_SENSITIVE)
            build()
        }.find()
    }

}