package com.aqrlei.androidjunior.fourthround.valueanimation

import android.animation.TypeEvaluator

/**
 * @author aqrlei on 2019/2/14
 */
class ScaleShapeEvaluator : TypeEvaluator<ScaleShape> {
    override fun evaluate(fraction: Float, startValue: ScaleShape, endValue: ScaleShape): ScaleShape {
        val x = startValue.x + fraction * (endValue.x - startValue.x)
        val y = startValue.y + fraction * (endValue.y - startValue.y)

        return ScaleShape(x, y)
    }
}