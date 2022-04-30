package com.ccn.objectboxviewer.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.viewbinding.ViewBinding

/**
 * ===========================================
 * 创 建 者：gao_chun
 * 版    本：1.0
 * 创建日期：2022-04-29.
 * 描    述：
 * ===========================================
 */

abstract class BaseBindingActivity<T : ViewBinding> : AppCompatActivity() {
    private lateinit var _binding: T
    protected val binding get() = _binding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding.root)
        initData()
    }

    protected abstract fun getViewBinding(): T

    abstract fun initData()

}
