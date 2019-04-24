package com.aqrlei.android.junior.thirdround

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * @author aqrlei on 2018/12/6
 */
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    companion object {
        private const val DEFAULT_HEIGHT = 200F
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CircleView)?.apply {
            paint.color = getColor(R.styleable.CircleView_color_circle, Color.GREEN)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            val mHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_HEIGHT,
                Resources.getSystem().displayMetrics
            ).toInt()
            setMeasuredDimension(widthSize, mHeight)
        } else {
            setMeasuredDimension(widthSize, heightSize)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("ViewEvent", "View: onTouchEvent ${event?.action}")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d("ViewEvent", "View: dispatchTouchEvent ${event?.action}")
        return super.dispatchTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val tempWidth = width - paddingRight - paddingLeft
        val tempHeight = height - paddingTop - paddingBottom
        val cx = width / 2.0F
        val cy = height / 2.0F
        val radius = Math.min(tempHeight, tempWidth) / 2.0F
        canvas.drawCircle(cx, cy, radius, paint)
    }
}