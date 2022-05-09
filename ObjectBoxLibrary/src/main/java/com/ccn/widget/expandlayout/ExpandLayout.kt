package com.ccn.widget.expandlayout

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.ccn.objectboxviewer.R

class ExpandLayout constructor(context: Context?, attrs: AttributeSet? = null) : FrameLayout(context!!, attrs) {
    interface State {
        companion object {
            const val COLLAPSED = 0
            const val COLLAPSING = 1
            const val EXPANDING = 2
            const val EXPANDED = 3
        }
    }

    var duration = DEFAULT_DURATION
    private var parallax = 0f
    private var expansion = 0f
    private var orientation = 0

    var state = 0
    private var interpolator: Interpolator = FastOutSlowInInterpolator()
    private var animator: ValueAnimator? = null
    private var listener: OnExpansionUpdateListener? = null
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        expansion = if (isExpanded) 1F else 0F
        bundle.putFloat(KEY_EXPANSION, expansion)
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        return bundle
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) {
        val bundle = parcelable as Bundle
        expansion = bundle.getFloat(KEY_EXPANSION)
        state = if (expansion == 1f) State.EXPANDED else State.COLLAPSED
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val size = if (orientation == LinearLayout.HORIZONTAL) width else height
        visibility = if (expansion == 0f && size == 0) GONE else VISIBLE
        val expansionDelta = size - Math.round(size * expansion)
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (animator != null) {
            animator!!.cancel()
        }
        super.onConfigurationChanged(newConfig)
    }

    var isExpanded: Boolean
        get() = state == State.EXPANDING || state == State.EXPANDED
        set(expand) {
            setExpanded(expand, true)
        }

    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }

    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }

    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    fun setExpanded(expand: Boolean, animate: Boolean) {
        if (expand == isExpanded) {
            return
        }
        val targetExpansion = if (expand) 1 else 0
        if (animate) {
            animateSize(targetExpansion)
        } else {
            setExpansion(targetExpansion.toFloat())
        }
    }

    fun setInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    fun getExpansion(): Float {
        return expansion
    }

    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) {
            return
        }

        // Infer state from previous value
        val delta = expansion - this.expansion
        when {
            expansion == 0f -> {
                state = State.COLLAPSED
            }
            expansion == 1f -> {
                state = State.EXPANDED
            }
            delta < 0 -> {
                state = State.COLLAPSING
            }
            delta > 0 -> {
                state = State.EXPANDING
            }
        }
        visibility = if (state == State.COLLAPSED) GONE else VISIBLE
        this.expansion = expansion
        requestLayout()
        if (listener != null) {
            listener!!.onExpansionUpdate(expansion, state)
        }
    }

    fun getParallax() = parallax

    private fun setParallax(parallax: Float) {
        // Make sure parallax is between 0 and 1
        var parallax = parallax
        parallax = Math.min(1f, Math.max(0f, parallax))
        this.parallax = parallax
    }

    fun getOrientation(): Int {
        return orientation
    }

    fun setOrientation(orientation: Int) {
        require(!(orientation < 0 || orientation > 1)) { "Orientation must be either 0 (horizontal) or 1 (vertical)" }
        this.orientation = orientation
    }

    fun setOnExpansionUpdateListener(listener: OnExpansionUpdateListener?) {
        this.listener = listener
    }

    private fun animateSize(targetExpansion: Int) {
        if (animator != null) {
            animator!!.cancel()
            animator = null
        }
        animator = ValueAnimator.ofFloat(expansion, targetExpansion.toFloat())
        with(animator!!) {
            setInterpolator(interpolator)
            setDuration(duration)
            addUpdateListener { valueAnimator -> setExpansion(valueAnimator.animatedValue as Float) }
            addListener(ExpansionListener(targetExpansion))
            start()
        }
    }

    interface OnExpansionUpdateListener {
        /**
         * @param expansionFraction 值介于0(折叠)和1(展开)之间，表示展开的进度
         * @param state             当前扩展状态
         */
        fun onExpansionUpdate(expansionFraction: Float, state: Int)
    }

    private inner class ExpansionListener(private val targetExpansion: Int) : AnimatorListener {
        private var canceled = false
        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == 0) State.COLLAPSING else State.EXPANDING
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == 0) State.COLLAPSED else State.EXPANDED
                setExpansion(targetExpansion.toFloat())
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }

    companion object {
        const val KEY_SUPER_STATE = "super_state"
        const val KEY_EXPANSION = "expansion"
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        private const val DEFAULT_DURATION = 300
    }

    init {
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            duration = a.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION)
            expansion = if (a.getBoolean(R.styleable.ExpandableLayout_el_expanded, false)) 1F else 0F
            orientation = a.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL)
            parallax = a.getFloat(R.styleable.ExpandableLayout_el_parallax, 1f)
            a.recycle()
            state = if (expansion == 0f) State.COLLAPSED else State.EXPANDED
            setParallax(parallax)
        }
    }
}