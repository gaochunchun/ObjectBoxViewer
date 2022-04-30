package com.ccn.objectbox

import android.app.Application
import com.ccn.objectbox.table.BusinessSampleTable
import com.ccn.objectbox.table.BusinessSampleTable2
import com.ccn.objectbox.table.ObjectBoxManager
import com.ccn.objectboxviewer.base.ObjectBoxViewer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBoxManager.init(this)

        val classes = ArrayList<Class<*>>().apply {
            add(BusinessSampleTable::class.java)
            add(BusinessSampleTable2::class.java)
        }
        ObjectBoxViewer.init(ObjectBoxManager.get(), classes)
    }
}