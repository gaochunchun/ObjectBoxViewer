package com.ccn.objectboxviewer

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import io.objectbox.Box


class ShowDataActivity : AppCompatActivity() {
    private var stringBuilder: StringBuilder? = null
    lateinit var boxStore: ArrayList<Box<*>>
    private var tableName = mutableListOf<String>()

    //private var viewById: TextView? = null
    //private var scaletext: TouchScaleTextView? = null
    private var title: TextView? = null
    private var webview: WebView? = null
    var setting: WebSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rview_main)
        initView()
        boxStore = ObjectBoxViewer.getBoxes()
        val classes = ObjectBoxViewer.getClasses()
        classes?.forEach {
            tableName.add(it.simpleName)
        }
        showDialog()
    }


    private fun initView() {
        initWebView()
        //scaletext = findViewById(R.id.scaletext)
        //viewById = findViewById(R.id.text)
        title = findViewById<TextView>(R.id.page_title)
        title!!.text = "表数据"
        findViewById<ImageView>(R.id.page_back).setOnClickListener {
            finish()
        }
    }

    private fun initWebView() {
        webview = findViewById(R.id.webview)
        setting = webview!!.settings
        setting?.setSupportZoom(true)
        setting?.textSize = WebSettings.TextSize.SMALLER
        setting?.builtInZoomControls = true;
        setting?.displayZoomControls = false;
        webview!!.isLongClickable = true
        webview!!.setOnLongClickListener { true }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("All Tables")
        builder.setCancelable(false)
        builder.setAdapter(object : ArrayAdapter<String?>(this, android.R.layout.simple_list_item_1, tableName.toList()) {}) { _, which ->
            showTableData(which)
        }
        builder.show()

    }

    private fun showTableData(which: Int) {
        title!!.text = tableName[which]
        if (which < boxStore.size) {
            val box = boxStore[which]
            doShowBoxData(box)
        }
    }

    private fun doShowBoxData(box: Box<*>) {
        stringBuilder = StringBuilder()
        box.all.filterNotNull().forEach {
            val o = it.toString()
            val value = o.substring(o.indexOf('(') + 1, o.length - 1)
            stringBuilder!!.append("<p>").append(value).append("</p>")
        }
        val data = "<html>${stringBuilder}</html>"
        webview!!.loadData(webViewBreak(data), "text/html; charset=UTF-8", null);
        //viewById!!.text = stringBuilder
        //scaletext!!.text = stringBuilder
    }

    //显示富文本，自动换行处理
    private fun webViewBreak(data: String): String {
        if (data.length > 7 && data.contains("<p>") && data.contains("</p>")) {
            return data.replace("<p>".toRegex(), "<p style=\"word-break:break-all\">")
        }
        return data
    }
}