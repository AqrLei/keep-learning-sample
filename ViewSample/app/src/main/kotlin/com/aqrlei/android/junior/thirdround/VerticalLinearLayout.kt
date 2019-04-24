package com.aqrlei.android.junior.thirdround

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams

/**
 * @author aqrlei on 2018/12/7
 */
class VerticalLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {


    inner class LayoutParams : ViewGroup.MarginLayoutParams {
        var position: Int = 0

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            context.obtainStyledAttributes(attrs, R.styleable.VerticalLinearLayout_Layout)?.run {
                position = getInteger(R.styleable.VerticalLinearLayout_Layout_layout_position, position)
                recycle()
            }
        }

        constructor(width: Int, height: Int) : super(width, height) {}

        constructor(p: ViewGroup.LayoutParams) : super(p)

        constructor(source: ViewGroup.MarginLayoutParams) : super(source)

        constructor(source: LayoutParams) : super(source)

    }

    /**
     * ViewGroup的子项是否延迟按下状态，通常像ListView这种可以滚动的布局返回是 true
     * ViewGroup中的方法
     * */
    override fun shouldDelayChildPressedState(): Boolean = false


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("ViewEvent", "ViewGroup: dispatchTouchEvent: ${ev?.action}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("ViewEvent", "ViewGroup: onInterceptTouchEvent: ${ev?.action}")
        return (this.getChildAt(0) as? CircleView)?.let { true } ?: super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("ViewEvent", "ViewGroup: onTouchEvent: ${event?.action}")
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        } else {
            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(getMaxChildWidth(), getTotalChildHeight())
            } else if (heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(widthSize, getTotalChildHeight())
            } else if (widthMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(getMaxChildWidth(), heightSize)
            } else {
                setMeasuredDimension(widthSize, heightSize)
            }

        }
    }

    /**
     * 为什么是measureWidth, XXXWidth之间的不同
     * */
    private fun getMaxChildWidth(): Int {
        var maxWidth = 0
        for (i in 0 until childCount) {
            maxWidth = if (getChildAt(i).measuredWidth > maxWidth) getChildAt(i).measuredWidth else maxWidth
        }
        return maxWidth
    }

    private fun getTotalChildHeight(): Int {
        var totalHeight = 0
        for (i in 0 until childCount) {
            totalHeight += getChildAt(i).measuredHeight
        }
        return totalHeight
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentHeight = 0
        for (i in 0 until childCount) {
            getChildAt(i).apply {
                val height = measuredHeight
                val width = measuredWidth
                layout(l, currentHeight, l + width, currentHeight + height)
                currentHeight += height
            }

        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is VerticalLinearLayout.LayoutParams
    }
}