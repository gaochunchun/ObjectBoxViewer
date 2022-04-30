package com.ccn.objectbox.table

import android.content.Context
import android.util.Log
import com.ccn.objectbox.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * ===========================================
 * 创 建 者：gao_chun
 * 版    本：1.0
 * 创建日期：2022-01-20.
 * 描    述：init ObjectBox
 * ===========================================
 */
object ObjectBoxManager {
    private const val DBNAME = "MeadJohnson"
    lateinit var boxStore: BoxStore private set

    @JvmStatic
    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .name(DBNAME)
            .build()
        if (BuildConfig.DEBUG) {
            //Terminal-> adb forward tcp:8090 tcp:8090  //浏览器录入：http://localhost:8090/index.html
            val started = AndroidObjectBrowser(boxStore).start(context)
            Log.e("ObjectBox", "Using ObjectBox ${BoxStore.getVersion()}  isDebug:$started ${boxStore.objectBrowserPort}")
        }
    }

    @JvmStatic
    fun get(): BoxStore {
        synchronized(ObjectBoxManager::class.java) {
            return boxStore
        }
    }

    @JvmStatic
    fun close() {
        if (!boxStore.isClosed) boxStore.close()
    }

}