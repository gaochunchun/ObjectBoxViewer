package com.ccn.objectboxviewer.activity

import android.app.AlertDialog
import android.webkit.WebSettings
import android.widget.ArrayAdapter
import com.ccn.objectboxviewer.base.BaseBindingActivity
import com.ccn.objectboxviewer.base.ObjectBoxViewer
import com.ccn.objectboxviewer.databinding.MainBinding
import io.objectbox.Box
import io.objectbox.BoxStore

class ShowDataActivity : BaseBindingActivity<MainBinding>() {
    private lateinit var stringBuilder: StringBuilder
    private lateinit var boxStore: ArrayList<Box<*>>
    private var tableName = mutableListOf<String>()
    private var setting: WebSettings? = null
    private lateinit var box: Box<*>

    override fun getViewBinding() = MainBinding.inflate(layoutInflater)

    override fun initData() {
        initWebView()
        binding.title.text = "表数据"
        binding.pageBack.setOnClickListener { finish() }
        binding.info.setOnClickListener { showInfoDialog() }
        binding.delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            with(builder) {
                setTitle("提示")
                setMessage("确认要删除该表中的数据吗？")
                setPositiveButton("确定") { _, _ ->
                    remove(box)
                    showBoxData(box)
                }
                setNegativeButton("取消") { _, _ -> }
                show()
            }
        }
        boxStore = ObjectBoxViewer.getBoxes()
        val classes = ObjectBoxViewer.getClasses()
        classes?.forEach {
            tableName.add(it.simpleName)
        }
        showDialog()
    }

    private fun initWebView() {
        setting = binding.webview.settings
        with(setting!!) {
            setSupportZoom(true)
            textZoom = 75
            builtInZoomControls = true
            displayZoomControls = false
        }
        binding.webview.isLongClickable = true
        binding.webview.setOnLongClickListener { true }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("All Tables")
            setAdapter(object : ArrayAdapter<String?>(context, android.R.layout.simple_list_item_1, tableName.toList()) {}) { _, which ->
                showTableData(which)
            }
            setOnCancelListener {
                it.dismiss()
                finish()
            }
            show()
        }
    }

    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("ObjectBox")
            setMessage("${BoxStore.getVersion()}\n${BoxStore.getVersionNative()}\n\nThe library author：gaochun")
            show()
        }
    }

    private fun showTableData(which: Int) {
        binding.title.text = tableName[which]
        if (which < boxStore.size) {
            box = boxStore[which]
            showBoxData(box)
        }
    }

    private fun showBoxData(box: Box<*>) {
        stringBuilder = StringBuilder()
        stringBuilder.append("<font color=\"blue\">总数据：${box.all.size}条</font>")
        box.all.filterNotNull().forEachIndexed { index, any ->
            val o = any.toString()
            val value = o.substring(o.indexOf('(') + 1, o.length - 1)
            when {
                index % 2 == 0 -> stringBuilder.append("<font color=\"green\"><p>").append(value).append("</p></font>")
                else -> stringBuilder.append("<p>").append(value).append("</p>")
            }
        }
        val data = "<html>${stringBuilder}</html>"
        binding.webview.loadData(webViewBreak(data), "text/html; charset=UTF-8", null);
    }

    private fun remove(box: Box<*>) {
        if (!box.isEmpty) box.removeAll()
    }

    //显示富文本，自动换行处理
    private fun webViewBreak(data: String): String {
        if (data.contains("<p>") && data.contains("</p>")) {
            return data.replace("<p>".toRegex(), "<p style=word-break:break-all>")
        }
        return data
    }
}