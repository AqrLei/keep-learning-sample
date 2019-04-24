package com.aqrlei.androidjunior.fourthround

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gesture.*

/**
 * @author aqrlei on 2019/2/15
 */
class GestureDetectorActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var preScale: Float = 1.0F
    private var curScale: Float = 1.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        initActionBar()
        initGesture()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        //gestureDetector.onGenericMotionEvent(event)
        return super.onGenericMotionEvent(event)
    }

    private fun initActionBar() {
        setSupportActionBar(titleToolBar)
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        titleToolBar.setNavigationOnClickListener { this.finish() }
    }

    private fun initGesture() {
        //如果不在主线程中创建,则需要 Looper.prepare(),因为内部的创建Handler()用于数据传输需要 Loop,也可以传一个Handler进去，Handler(Looper.getMainLooper())
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            // OnContextClickListener
            override fun onContextClick(e: MotionEvent?): Boolean {
                // 监听外部设备按钮
                Log.d("Gesture", "onContextClick")
                return super.onContextClick(e)
            }

            //OnDoubleTapListener
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                //当发生双击时不会触发,两次点击在300ms(ViewConfiguration.DOUBLE_TAP_TIMEOUT)之内 ，通过   Handler().sendEmptyMessageDelayed()实现
                Log.d("Gesture", "onSingleTapConfirmed")
                return super.onSingleTapConfirmed(e)
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                //当第二次 action 为 "0-down"的时候就触发此方法
                Log.d("Gesture", "onDoubleTap")
                return super.onDoubleTap(e)
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                //二次点击的Action 变化时会触发，如果要在 action 为"1-up"的时候处理，就在这个方法里实现逻辑
                Log.d("Gesture", "onDoubleTapEvent - ${e.action}")
                return super.onDoubleTapEvent(e)
            }


            //OnGestureListener
            override fun onDown(e: MotionEvent?): Boolean {
                //按下
                Log.d("Gesture", "onDown")
                return super.onDown(e)
            }

            override fun onShowPress(e: MotionEvent?) {
                //延时回调 100(ViewConfiguration.TAP_TIMEOUT)后还是处于 action "0-down"就会触发
                Log.d("Gesture", "onShowPress")
                super.onShowPress(e)
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                //双击时，onDoubleTapUp会触发，onSingleTapUp会在第一次"down"时触发
                Log.d("Gesture", "onSingleTapUp")
                return super.onSingleTapUp(e)
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                //快速滑动
                Log.d("Gesture", "onScroll")
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onLongPress(e: MotionEvent?) {
                //长按 500(ViewConfiguration.DEFAULT_LONG_PRESS_TIMEOUT)后还是处于 action "0-down"就会触发
                Log.d("Gesture", "onLongPress")
                super.onLongPress(e)
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                //快速滑动后抬起
                Log.d("Gesture", "onFling")
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })

        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                Log.d(
                    "ScaleGesture",
                    "onScaleBegin: scaleFactor-${detector.scaleFactor} focusX-${detector.focusX} focusY-${detector.focusY}"
                )
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {

                //保证连续性
                curScale = detector.scaleFactor * preScale
                if (curScale > 5 || curScale < 0.1) {
                    curScale = preScale
                    return true
                }
                testV.scaleY = curScale
                testV.scaleX = curScale
                preScale = curScale
                Log.d(
                    "ScaleGesture",
                    "onScale: scaleFactor-${detector.scaleFactor} focusX-${detector.focusX} focusY-${detector.focusY}"
                )
                return super.onScale(detector)
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                Log.d(
                    "ScaleGesture",
                    "onScaleEnd: scaleFactor-${detector.scaleFactor} focusX-${detector.focusX} focusY-${detector.focusY}"
                )

            }

        })
    }
}