package com.ccn.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

/**
 * ===========================================
 * 创 建 者：gao_chun
 * 版    本：1.0
 * 创建日期：2022-04-29.
 * 描    述：浮动拽托按钮布局
 * ===========================================
 */
class FloatLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mDragger: ViewDragHelper? = null
    private var mDragView: View? = null

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : this(context, attrs, 0) {
        mDragger = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                //mEdgeTrackerView禁止直接移动
                //return child == mDragView || child == mAutoBackView;
                return child === mDragView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return top
            }

            //手指释放的时候回调
            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                //mAutoBackView手指释放时可以自动回去
                /*if (releasedChild == mAutoBackView) {
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }*/
            }

            //在边界拖动时回调
            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                //mDragger.captureChildView(mEdgeTrackerView, pointerId);
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return measuredWidth - child.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return measuredHeight - child.measuredHeight
            }
        })
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return mDragger!!.shouldInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragger!!.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (mDragger!!.continueSettling(true)) {
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        getChildAt(0).setOnTouchListener { v, _ ->
            mDragView!!.alpha = 0.3f
            false
        }
        mDragView = getChildAt(1)
        mDragView!!.setOnTouchListener { v, _ ->
            mDragView!!.setAlpha(1.0f)
            false
        }
    }
}