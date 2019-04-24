package com.aqrlei.android.junior.thirdround

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verticalL.setOnTouchListener { v, event ->
            Log.d("ViewEvent", "ViewGroup:onTouch ${event.action}")
            false
        }
        circleView.setOnTouchListener { v, event ->
            Log.d("ViewEvent", "View:onTouch ${event.action}")
            false
        }
        this.window.decorView.setOnTouchListener { v, event ->

            Log.d("ViewEvent", "DecorView:onTouch ${event.action}")
            false
        }

        /**
         * onTouchEvent ACTION_UP后回调，
         * */
        circleView.setOnClickListener {
            Log.d("ViewEvent", "View:onClickListener")
        }
        /**
         * onTouchEvent  ACTION_DOWN 一般延迟500ms后，还是press状态时回调
         * */
        circleView.setOnLongClickListener {
            Log.d("ViewEvent", "View:onLongClickListener")
            /**
             * 此处返回的值，true表示事件消费完毕，click不再响应,false表示还可以继续消费
             *
             * */
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("ViewEvent", "Activity : onTouchEvent ${event?.action}")
        return super.onTouchEvent(event)
    }
}
