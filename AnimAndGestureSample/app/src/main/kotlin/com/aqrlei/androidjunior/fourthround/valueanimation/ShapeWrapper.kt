package com.aqrlei.androidjunior.fourthround.valueanimation

import android.view.View

/**
 * @author aqrlei on 2019/2/14
 */
class ShapeWrapper(private val target: View) {

    fun setShape(shape: ScaleShape) {
        target.layoutParams.apply {
            width = shape.x.toInt()
            height = shape.y.toInt()
        }
        target.requestLayout()
    }

    fun getShape(): ScaleShape {
        return ScaleShape(target.width.toFloat(), target.height.toFloat())
    }
}