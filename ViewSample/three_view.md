ThirdRound
==========
![](../images/android_junior_three_customize_view.png)
attrs
------
  -  *values* 文件夹中定义**attr**属性文件
   ````
   <resources>
       <declare-styleable name="CircleView">
           <attr name="color_circle" format="reference|color"/>
       </declare-styleable>
   </resources>
   ````
  - attr的format取值类型( *属性定义时可以指定多种类型* )

   | typeName       | Description        |
   |:--------------:|:------------------:|
   | reference      | 参考某一资源id       |
   | color          | 颜色值              |
   | flag           | 可用于位或运算       |
   | enum           | 枚举值              |
   | fraction       | 百分数              |
   | boolean        | 布尔值              |
   | dimension      | 尺寸值              |
   | float          | 浮点数              |
   | integer        | 整数               |
   | string         | 字符串              |

   ````
       <attr name="orientation">
           <enum name="horizontal" value="0" />
           <enum name="vertical" value="1" />
       </attr>
   ````

   ````
           <attr name="windowSoftInputMode">
               <flag name="stateUnspecified" value="0" />
               <flag name="stateUnchanged" value="1" />
               <flag name="stateHidden" value="2" />
               <flag name="stateAlwaysHidden" value="3" />
               <flag name="stateVisible" value="4" />
               <flag name="stateAlwaysVisible" value="5" />
               <flag name="adjustUnspecified" value="0x00" />
               <flag name="adjustResize" value="0x10" />
               <flag name="adjustPan" value="0x20" />
               <flag name="adjustNothing" value="0x30" />
           </attr>
   ````
- xml中使用自定义的attr属性值

  ````
  <com.aqrlei.android.junior.thirdround.CircleView
                  android:layout_width="wrap_content"
                  android:layout_height="wrap_content"
                  android:padding="20dp"
                  app:color_circle="@color/colorAccent"/>
  ````

- 代码中获取自定义的attr属性值
  ````
  context.obtainStyledAttributes(attrs, R.styleable.CircleView)?.apply {
            paint.color = getColor(R.styleable.CircleView_color_circle, Color.GREEN)
            recycle()
        }
  ````



onMeasure
---------

   View只要对自身的MeasureSpec进行处理即可，而ViewGroup中的onMeasure除了对本身的
   MeasureSpec要进行处理，还要调用ChildView的测量的相关方法,
   如:<br>  `measureChildren(widthMeasureSpec, heightMeasureSpec)`
  - #### measureSpecMode <br>

     *availableSize*指父容器中剩余可用的全部空间

   | parentLayoutParams        | parentSpecMode        | childLayoutParams  |childSpecMode  |childSpecSize  |
   |:-------------------------:|:---------------------:|:------------------:|:-------------:|:-------------:|
   | match_parent              | EXACTLY               | match_parent       | EXACTLY       | childSize     |
   | match_parent              | EXACTLY               | wrap_content       | AT_MOST       | availableSize |
   | match_parent              | EXACTLY               | dp                 | EXACTLY       | childSize     |
   | wrap_parent               | AT_MOST               | wrap_content       | AT_MOST       | availableSize |
   | wrap_parent               | AT_MOST               | match_parent       | AT_MOST       | availableSize |
   | wrap_parent               | AT_MOST               | dp                 | EXACTLY       | childSize     |
   | dp                        | EXACTLY               | match_parent       | EXACTLY       | childSize     |
   | dp                        | EXACTLY               | dp                 | EXACTLY       | childSize     |
   | dp                        | EXACTLY               | wrap_content       | AT_MOST       | availableSize |
   |                           | UNSPECIFIED           | match_parent       | UNSPECIFIED   | UNSPECIFIED   |
   |                           | UNSPECIFIED           | wrap_content       | UNSPECIFIED   | UNSPECIFIED   |
   |                           | UNSPECIFIED           | dp                 | EXACTLY       | childSize     |


  - example(*只对Height进行了处理*)

    据表可以看到在子View的 SpecMode 为 AT_MOST的时候，SpecSize都为 availableSize,
    即使LayoutParams设置为*wrap_content(AT_MOST)*。所以在自定义View在重写*onMeasure*时，
    对AT_MOST要进行特殊处理来支持wrap_content.可以设置一个默认的SIZE，或者也可以通过计算得到SIZE



   ````
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
   ````
onLayout
--------

一般自定义View时，ViewGroup需要重写onLayout()方法，经过自定义处理后在其中调用childView的layout方法。
View一般则不需要重写onLayout()

` protected void onLayout(boolean changed, int left, int top, int right, int bottom) {  }`
`

| paramName  | Description                               |
|:----------:|:-----------------------------------------:|
| changed    | 见View.java的 ` protected boolean setFrame(int left, int top, int right, int bottom) `            |
| left       | View左边界距父布局的左边界距离                |
| top        | View上边界距父布局的上边界距离                |
| right      | View右边界距父布局的右边界距离                |
| bottom     | View底边界距父布局的底边界距离                |


onDraw
------
 自定义View时，一般ViewGroup不需要重写onDraw()方法。
 View则需要重写onDraw()方法，画出相应的内容


ViewGroup扩展
-----------

 - ViewGroup的子项是否延迟按下状态，通常像ListView这种可以滚动的布局返回是 true
   属于ViewGroup中的方法
 ` public boolean shouldDelayChildPressedState() { return true; } `
 - 自定义ViewGroup中的LayoutParams属性,使其在childView中使用, 如*LinearLayout*中的*layout_weight*
   1. 定义attr属性文件

    ````
    <declare-styleable name="VerticalLinearLayout_Layout">
            <attr name="layout_position" format="enum">
                <enum name="left" value="0"/>
                <enum name="top" value="1"/>
                <enum name="right" value="2"/>
                <enum name="bottom" value="3"/>
            </attr>
        </declare-styleable
    ````
    2. 自定义LayoutParams(内部类),获取属性值

    ````
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
    ````
    3. 重写ViewGroup中的相关方法

    ````
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
    ````

