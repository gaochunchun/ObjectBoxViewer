package com.ccn.objectbox

import android.app.Application
import com.ccn.objectboxviewer.ObjectBoxViewer
import io.objectbox.BoxStore

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