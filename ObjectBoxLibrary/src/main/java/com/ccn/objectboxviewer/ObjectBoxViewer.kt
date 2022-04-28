package com.ccn.objectboxviewer

import io.objectbox.Box
import io.objectbox.BoxStore

/**
 * ===========================================
 * 创 建 者：gao_chun
 * 版    本：1.0
 * 创建日期：2022-01-20.
 * 描    述：init ObjectBoxViewer
 * ===========================================
 */
object ObjectBoxViewer {
    lateinit var boxStore: BoxStore private set
    private var mBoxStore: BoxStore? = null
    private var classes: ArrayList<Class<*>>? = null
    private val boxes = ArrayList<Box<*>>()

    @JvmStatic
    fun init(boxStore: BoxStore, classes: ArrayList<Class<*>>) {
        this.mBoxStore = boxStore
        classes.forEach {
            this.classes = classes
            val box = mBoxStore?.boxFor(it)
            box?.let { boxes += box }
        }
    }

    @JvmStatic
    fun get(): BoxStore {
        synchronized(ObjectBoxViewer::class.java) {
            return boxStore
        }
    }

    fun getBoxes() = boxes

    fun getClasses() = classes
}