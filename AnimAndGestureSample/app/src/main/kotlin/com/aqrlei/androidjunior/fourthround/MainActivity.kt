package com.aqrlei.androidjunior.fourthround

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import com.aqrlei.androidjunior.fourthround.valueanimation.ScaleShape
import com.aqrlei.androidjunior.fourthround.valueanimation.ScaleShapeEvaluator
import com.aqrlei.androidjunior.fourthround.valueanimation.ShapeWrapper
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var useCode: Boolean = false
    private val valueAnimator: ValueAnimator
        get() = if (useCode) valueAnimatorCode
        else valueAnimatorXml
    private val valueAnimatorCode =
        ValueAnimator.ofInt(Color.parseColor("#008577"), Color.parseColor("#D81B60")).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
        }
    private lateinit var valueAnimatorXml: ValueAnimator
    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private val valueAnimatorOfObject =
        ValueAnimator.ofObject(ScaleShapeEvaluator(), ScaleShape(0.5F, 0.2F), ScaleShape(2.0F, 3.0F)).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }

    private var animatorValueUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private val objectAnimator: Animator
        get() = if (useCode) objectAnimatorCode
        else objectAnimatorXml
    private lateinit var objectAnimatorCode: Animator

    private lateinit var objectAnimatorXml: Animator


    private lateinit var objectAnimatorOfObject: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActionBar()
        initAnimation()
        initListener()
    }


    private fun initActionBar() {
        setSupportActionBar(titleToolBar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        titleToolBar.setNavigationOnClickListener {
            startActivity(Intent(this, GestureDetectorActivity::class.java))
        }
    }

    private fun initAnimation() {
        valueAnimatorXml = (AnimatorInflater.loadAnimator(this, R.animator.animator_int) as ValueAnimator)
        objectAnimatorXml = AnimatorInflater.loadAnimator(this, R.animator.animator_object_rotate).apply {
            setTarget(testV)
        }
        objectAnimatorOfObject = ObjectAnimator.ofObject(
            ShapeWrapper(testV),
            "shape",
            ScaleShapeEvaluator(),
            ScaleShape(100F, 100F),
            ScaleShape(500F, 500F)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            duration = 2000
            interpolator = LinearInterpolator()
        }
        objectAnimatorCode = ObjectAnimator.ofFloat(testV, "rotationY", 0f, -360F).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            duration = 2000
            interpolator = LinearInterpolator()
        }
    }

    private fun initListener() {
        topTab?.run {
            addTab(this.newTab().setText("Alpha"))
            addTab(this.newTab().setText("Rotate"))
            addTab(this.newTab().setText("Scale"))
            addTab(this.newTab().setText("Translate"))
            addTab(this.newTab().setText("AnimationDrawable"))
            addTab(this.newTab().setText("ValueAnimator"))
            addTab(this.newTab().setText("ValueAnimatorOfObject"))
            addTab(this.newTab().setText("ObjectAnimator"))
            addTab(this.newTab().setText("ObjectAnimatorOfObject"))
            addTab(this.newTab().setText("Set"))
            this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    selectAnimationToShow(p0?.position ?: -1)
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {

                }
            })
        }
        toggleB.setOnCheckedChangeListener { _, isChecked ->
            valueAnimator.removeUpdateListener(animatorUpdateListener)
            valueAnimatorOfObject.removeUpdateListener(animatorValueUpdateListener)
            objectAnimator.setTarget(null)
            objectAnimatorOfObject.target = null
            useCode = isChecked
        }
    }

    private fun selectAnimationToShow(which: Int) {
        val alphaAnim = AlphaAnimation(1.0F, 0.5F).apply {
            duration = 3000
        }
        val rotateAnim =
            RotateAnimation(-225F, 0F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F).apply {
                duration = 3000
                interpolator = LinearInterpolator()
            }
        val scaleAnim = ScaleAnimation(
            1.0F,
            0.5F,
            1.0F,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F
        ).apply {
            duration = 3000
        }
        val translateAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT,
            1.0F,
            Animation.RELATIVE_TO_SELF,
            0F,
            Animation.RELATIVE_TO_PARENT,
            1.0F,
            Animation.RELATIVE_TO_SELF,
            0F
        ).apply {
            duration = 3000
        }
        valueAnimator.removeUpdateListener(animatorUpdateListener)
        valueAnimatorOfObject.removeUpdateListener(animatorValueUpdateListener)
        objectAnimator.setTarget(null)
        objectAnimatorOfObject.target = null
        when (which) {
            4 -> {
                (testV.drawable as? AnimationDrawable)?.start()
            }
            5 -> {
                animatorUpdateListener = animatorUpdateListener ?: ValueAnimator.AnimatorUpdateListener {
                    testV.background = ColorDrawable(it.animatedValue as Int)
                }
                valueAnimator.apply {
                    addUpdateListener(animatorUpdateListener)
                    start()
                }
            }
            6 -> {
                animatorValueUpdateListener = animatorValueUpdateListener ?: ValueAnimator.AnimatorUpdateListener {
                    (it.animatedValue as? ScaleShape)?.run {
                        testV.scaleX = x
                        testV.scaleY = y
                    }
                }
                valueAnimatorOfObject.apply {
                    addUpdateListener(animatorValueUpdateListener)
                    start()
                }
            }
            7 -> {
                objectAnimator.start()
            }
            8 -> {
                objectAnimatorOfObject.start()
            }
            else -> {
                val animation = when (which) {
                    0 -> {
                        if (useCode)
                            alphaAnim
                        else
                            AnimationUtils.loadAnimation(this, R.anim.alpha_anim)
                    }
                    1 -> {
                        if (useCode)
                            rotateAnim
                        else
                            AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
                    }
                    2 -> {
                        if (useCode)
                            scaleAnim
                        else
                            AnimationUtils.loadAnimation(this, R.anim.scale_anim)
                    }
                    3 -> {
                        if (useCode)
                            translateAnim
                        else
                            AnimationUtils.loadAnimation(this, R.anim.translate_anim)
                    }

                    else -> {
                        if (useCode)
                            AnimationSet(false).apply {

                                addAnimation(alphaAnim.apply {
                                    repeatCount = Animation.INFINITE
                                })
                                addAnimation(rotateAnim.apply {
                                    repeatCount = Animation.INFINITE
                                })
                                addAnimation(scaleAnim.apply {
                                    repeatCount = Animation.INFINITE
                                })
                                addAnimation(translateAnim.apply {
                                    repeatCount = Animation.INFINITE
                                })
                            }
                        else
                            AnimationUtils.loadAnimation(this, R.anim.set_anim)
                    }
                }
                testV.startAnimation(animation)
            }
        }

    }
}
