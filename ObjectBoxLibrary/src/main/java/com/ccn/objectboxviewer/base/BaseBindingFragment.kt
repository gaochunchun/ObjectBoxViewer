package com.ccn.objectboxviewer.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.ViewBinding

/**
 * ===========================================
 * 创 建 者：gao_chun
 * 版    本：1.0
 * 创建日期：2022-04-29.
 * 描    述：
 * ===========================================
 */

abstract class BaseBindingFragment<T : ViewBinding> : Fragment() {
    private lateinit var _binding: T
    protected val binding get() = _binding;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getViewBinding(inflater, container)
        return _binding.root
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T
}