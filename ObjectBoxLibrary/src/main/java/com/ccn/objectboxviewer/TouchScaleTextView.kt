package com.ccn.objectboxviewer

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent

/**
 * ===========================================
 * https://www.jianshu.com/p/00fbe09d10c4
 * 版    本：1.0
 * 创建日期：2022-04-27.
 * 描    述：
 * ===========================================
 */
class TouchScaleTextView : AppCompatTextView {

    var mode = 0
    var mOldDist = 0f
    var mTextSize = 0f

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
    }

    constructor(context: Context, attributes: AttributeSet, defStyle: Int) : super(context, attributes, defStyle) {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mTextSize == 0f) {
            mTextSize = textSize

        }
        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mOldDist = 0f
                mode = 1
            }
            MotionEvent.ACTION_UP -> {
                mode = 0
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode -= 1
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mOldDist = spacing(event)
                mode += 1
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode >= 2) {
                    val newDist = spacing(event)
                    if (Math.abs(newDist - mOldDist) > 50) {
                        zoom(newDist / mOldDist)
                        mOldDist = newDist
                    }
                }
            }
        }
        return true
    }

    fun zoom(f: Float) {
        mTextSize *= f
        setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
    }

    fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getX(0) - event.getX(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }


}
