package com.ccn.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ccn.objectboxviewer.R

/**
 * ===========================================
 * 创 建 者：gao_chun
 * 版    本：1.0
 * 创建日期：2022-05-10.
 * 描    述：绘制横向 竖向 虚线
 * ===========================================
 */
class DrawDividerView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    companion object {
        var ORIENTATION_HORIZONTAL = 0
        var ORIENTATION_VERTICAL = 1
    }

    private val mPaint: Paint
    private var orientation = 0
    fun setBgColor(color: Int) {
        mPaint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == ORIENTATION_HORIZONTAL) {
            val center = height * 0.5f
            canvas.drawLine(0f, center, width.toFloat(), center, mPaint)
        } else {
            val center = width * 0.5f
            canvas.drawLine(center, 0f, center, height.toFloat(), mPaint)
        }
    }

    init {
        val dashGap: Int
        val dashLength: Int
        val dashThickness: Int
        val color: Int
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DividerView, 0, 0)
        try {
            with(a) {
                getDimensionPixelSize(R.styleable.DividerView_dashGap, 5).also { dashGap = it }
                getDimensionPixelSize(R.styleable.DividerView_dashLength, 5).also { dashLength = it }
                getDimensionPixelSize(R.styleable.DividerView_dashThickness, 3).also { dashThickness = it }
                getColor(R.styleable.DividerView_divider_line_color, -0x1000000).also { color = it }
                getInt(R.styleable.DividerView_divider_orientation, ORIENTATION_HORIZONTAL).also { orientation = it }
            }
        } finally {
            a.recycle()
        }
        mPaint = Paint()
        with(mPaint) {
            isAntiAlias = true
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = dashThickness.toFloat()
            pathEffect = DashPathEffect(floatArrayOf(dashGap.toFloat(), dashLength.toFloat()), 0F)
        }
    }
}