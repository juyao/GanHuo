package com.juyao.ganhuo.widget

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable


import uk.co.senab.photoview.PhotoView

/**
 * Created by juyao on 2017/6/2 at 09:40.\n
 * 邮箱:juyao0909@gmail.com
 */


class SmoothImageView : PhotoView {
    private var mOriginalWidth: Int = 0
    private var mOriginalHeight: Int = 0
    private var mOriginalLocationX: Int = 0
    private var mOriginalLocationY: Int = 0
    private var mState = STATE_NORMAL
    private var mSmoothMatrix: Matrix? = null
    private var mBitmap: Bitmap? = null
    private var mTransformStart = false
    private var mTransfrom: Transfrom? = null
    private val mBgColor = 0xFF000000.toInt()
    private var mBgAlpha = 0
    private var mPaint: Paint? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }
    /**
     * PhotoView:1.3.0'版本
     */
    //        @Override
    //    protected void init() {
    //        super.init();
    //        mSmoothMatrix = new Matrix();
    //        mPaint = new Paint();
    //        mPaint.setColor(mBgColor);
    //        mPaint.setStyle(Style.FILL);
    ////		setBackgroundColor(mBgColor);
    //    }

    /**
     * photoview:library:1.2.2'版本
     */
    private fun init() {
        mSmoothMatrix = Matrix()
        mPaint = Paint()
        mPaint!!.color = mBgColor
        mPaint!!.style = Paint.Style.FILL
        //		setBackgroundColor(mBgColor);
    }

    fun setOriginalInfo(width: Int, height: Int, locationX: Int, locationY: Int) {
        mOriginalWidth = width
        mOriginalHeight = height
        mOriginalLocationX = locationX
        mOriginalLocationY = locationY
        // 因为是屏幕坐标，所以要转换为该视图内的坐标，因为我所用的该视图是MATCH_PARENT，所以不用定位该视图的位置,如果不是的话，还需要定位视图的位置，然后计算mOriginalLocationX和mOriginalLocationY
        mOriginalLocationY = mOriginalLocationY - getStatusBarHeight(context)
    }

    /**
     * 用于开始进入的方法。 调用此方前，需已经调用过setOriginalInfo
     */
    fun transformIn() {
        mState = STATE_TRANSFORM_IN
        mTransformStart = true
        invalidate()
    }

    /**
     * 用于开始退出的方法。 调用此方前，需已经调用过setOriginalInfo
     */
    fun transformOut() {
        mState = STATE_TRANSFORM_OUT
        mTransformStart = true
        invalidate()
    }

    private inner class Transfrom {
        internal var startScale: Float = 0.toFloat()// 图片开始的缩放值
        internal var endScale: Float = 0.toFloat()// 图片结束的缩放值
        internal var scale: Float = 0.toFloat()// 属性ValueAnimator计算出来的值
        internal var startRect: SmoothImageView.LocationSizeF? = null// 开始的区域
        internal var endRect: SmoothImageView.LocationSizeF? = null// 结束的区域
        internal var rect: SmoothImageView.LocationSizeF? = null// 属性ValueAnimator计算出来的值

        internal fun initStartIn() {
            scale = startScale
            try {
                rect = startRect!!.clone() as SmoothImageView.LocationSizeF
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }

        }

        internal fun initStartOut() {
            scale = endScale
            try {
                rect = endRect!!.clone() as SmoothImageView.LocationSizeF
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }

        }

    }

    /**
     * 初始化进入的变量信息
     */
    private fun initTransform() {
        if (drawable == null) {
            return
        }

        //防止转换失败
        if (drawable is ColorDrawable) return

        if (mBitmap == null || mBitmap!!.isRecycled) {
            mBitmap = (drawable as GlideBitmapDrawable).bitmap
        }
        //防止mTransfrom重复的做同样的初始化
        if (mTransfrom != null) {
            return
        }
        if (width == 0 || height == 0) {
            return
        }
        mTransfrom = Transfrom()

        /** 下面为缩放的计算  */
        /* 计算初始的缩放值，初始值因为是CENTR_CROP效果，所以要保证图片的宽和高至少1个能匹配原始的宽和高，另1个大于 */
        val xSScale = mOriginalWidth / mBitmap!!.width.toFloat()
        val ySScale = mOriginalHeight / mBitmap!!.height.toFloat()
        val startScale = if (xSScale > ySScale) xSScale else ySScale
        mTransfrom!!.startScale = startScale
        /* 计算结束时候的缩放值，结束值因为要达到FIT_CENTER效果，所以要保证图片的宽和高至少1个能匹配原始的宽和高，另1个小于 */
        val xEScale = width / mBitmap!!.width.toFloat()
        val yEScale = height / mBitmap!!.height.toFloat()
        val endScale = if (xEScale < yEScale) xEScale else yEScale
        mTransfrom!!.endScale = endScale

        /**
         * 下面计算Canvas Clip的范围，也就是图片的显示的范围，因为图片是慢慢变大，并且是等比例的，所以这个效果还需要裁减图片显示的区域
         * ，而显示区域的变化范围是在原始CENTER_CROP效果的范围区域
         * ，到最终的FIT_CENTER的范围之间的，区域我用LocationSizeF更好计算
         * ，他就包括左上顶点坐标，和宽高，最后转为Canvas裁减的Rect.
         */
        /* 开始区域 */
        mTransfrom!!.startRect =LocationSizeF()
        mTransfrom!!.startRect!!.left = mOriginalLocationX.toFloat()
        mTransfrom!!.startRect!!.top = mOriginalLocationY.toFloat()
        mTransfrom!!.startRect!!.width = mOriginalWidth.toFloat()
        mTransfrom!!.startRect!!.height = mOriginalHeight.toFloat()
        /* 结束区域 */
        mTransfrom!!.endRect = LocationSizeF()
        val bitmapEndWidth = mBitmap!!.width * mTransfrom!!.endScale// 图片最终的宽度
        val bitmapEndHeight = mBitmap!!.height * mTransfrom!!.endScale// 图片最终的宽度
        mTransfrom!!.endRect!!.left = (width - bitmapEndWidth) / 2
        mTransfrom!!.endRect!!.top = (height - bitmapEndHeight) / 2
        mTransfrom!!.endRect!!.width = bitmapEndWidth
        mTransfrom!!.endRect!!.height = bitmapEndHeight

        mTransfrom!!.rect =LocationSizeF()
    }

    private inner class LocationSizeF : Cloneable {
        internal var left: Float = 0.toFloat()
        internal var top: Float = 0.toFloat()
        internal var width: Float = 0.toFloat()
        internal var height: Float = 0.toFloat()

        override fun toString(): String {
            return "[left:$left top:$top width:$width height:$height]"
        }

        @Throws(CloneNotSupportedException::class)
        public override fun clone(): Any {
            // TODO Auto-generated method stub
            return super.clone()
        }

    }

    /* 下面实现了CENTER_CROP的功能 的Matrix，在优化的过程中，已经不用了 */
    private fun getCenterCropMatrix() {
        if (drawable == null) {
            return
        }
        if (mBitmap == null || mBitmap!!.isRecycled) {
            mBitmap = (drawable as BitmapDrawable).bitmap
        }
        /* 下面实现了CENTER_CROP的功能 */
        val xScale = mOriginalWidth / mBitmap!!.width.toFloat()
        val yScale = mOriginalHeight / mBitmap!!.height.toFloat()
        val scale = if (xScale > yScale) xScale else yScale
        mSmoothMatrix!!.reset()
        mSmoothMatrix!!.setScale(scale, scale)
        mSmoothMatrix!!.postTranslate(-(scale * mBitmap!!.width / 2 - mOriginalWidth / 2), -(scale * mBitmap!!.height / 2 - mOriginalHeight / 2))
    }

    private fun getBmpMatrix() {
        if (drawable == null) {
            return
        }
        if (mTransfrom == null) {
            return
        }
        if (mBitmap == null || mBitmap!!.isRecycled) {
            mBitmap = (drawable as BitmapDrawable).bitmap
        }
        /* 下面实现了CENTER_CROP的功能 */
        mSmoothMatrix!!.setScale(mTransfrom!!.scale, mTransfrom!!.scale)
        mSmoothMatrix!!.postTranslate(-(mTransfrom!!.scale * mBitmap!!.width / 2 - mTransfrom!!.rect!!.width / 2),
                -(mTransfrom!!.scale * mBitmap!!.height / 2 - mTransfrom!!.rect!!.height / 2))
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            return  // couldn't resolve the URI
        }

        if (mState == STATE_TRANSFORM_IN || mState == STATE_TRANSFORM_OUT) {
            if (mTransformStart) {
                initTransform()
            }
            if (mTransfrom == null) {
                super.onDraw(canvas)
                return
            }

            if (mTransformStart) {
                if (mState == STATE_TRANSFORM_IN) {
                    mTransfrom!!.initStartIn()
                } else {
                    mTransfrom!!.initStartOut()
                }
            }

            if (mTransformStart) {
                Log.d("Dean", "mTransfrom.startScale:" + mTransfrom!!.startScale)
                Log.d("Dean", "mTransfrom.startScale:" + mTransfrom!!.endScale)
                Log.d("Dean", "mTransfrom.scale:" + mTransfrom!!.scale)
                Log.d("Dean", "mTransfrom.startRect:" + mTransfrom!!.startRect!!.toString())
                Log.d("Dean", "mTransfrom.endRect:" + mTransfrom!!.endRect!!.toString())
                Log.d("Dean", "mTransfrom.rect:" + mTransfrom!!.rect.toString())
            }

            mPaint!!.alpha = mBgAlpha
            canvas.drawPaint(mPaint!!)

            val saveCount = canvas.saveCount
            canvas.save()
            // 先得到图片在此刻的图像Matrix矩阵
            getBmpMatrix()
            canvas.translate(mTransfrom!!.rect!!.left, mTransfrom!!.rect!!.top)
            canvas.clipRect(0f, 0f, mTransfrom!!.rect!!.width, mTransfrom!!.rect!!.height)
            canvas.concat(mSmoothMatrix)
            drawable.draw(canvas)
            canvas.restoreToCount(saveCount)
            if (mTransformStart) {
                mTransformStart = false
                startTransform(mState)
            }
        } else {
            //当Transform In变化完成后，把背景改为黑色，使得Activity不透明
            mPaint!!.alpha = 255
            canvas.drawPaint(mPaint!!)
            super.onDraw(canvas)
        }
    }

    private fun startTransform(state: Int) {
        if (mTransfrom == null) {
            return
        }
        val valueAnimator = ValueAnimator()
        valueAnimator.duration = 300
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        if (state == STATE_TRANSFORM_IN) {
            val scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransfrom!!.startScale, mTransfrom!!.endScale)
            val leftHolder = PropertyValuesHolder.ofFloat("left", mTransfrom!!.startRect!!.left, mTransfrom!!.endRect!!.left)
            val topHolder = PropertyValuesHolder.ofFloat("top", mTransfrom!!.startRect!!.top, mTransfrom!!.endRect!!.top)
            val widthHolder = PropertyValuesHolder.ofFloat("width", mTransfrom!!.startRect!!.width, mTransfrom!!.endRect!!.width)
            val heightHolder = PropertyValuesHolder.ofFloat("height", mTransfrom!!.startRect!!.height, mTransfrom!!.endRect!!.height)
            val alphaHolder = PropertyValuesHolder.ofInt("alpha", 0, 255)
            valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder)
        } else {
            val scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransfrom!!.endScale, mTransfrom!!.startScale)
            val leftHolder = PropertyValuesHolder.ofFloat("left", mTransfrom!!.endRect!!.left, mTransfrom!!.startRect!!.left)
            val topHolder = PropertyValuesHolder.ofFloat("top", mTransfrom!!.endRect!!.top, mTransfrom!!.startRect!!.top)
            val widthHolder = PropertyValuesHolder.ofFloat("width", mTransfrom!!.endRect!!.width, mTransfrom!!.startRect!!.width)
            val heightHolder = PropertyValuesHolder.ofFloat("height", mTransfrom!!.endRect!!.height, mTransfrom!!.startRect!!.height)
            val alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0)
            valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder)
        }

        valueAnimator.addUpdateListener { animation ->
            mTransfrom!!.scale = animation.getAnimatedValue("scale") as Float
            mTransfrom!!.rect!!.left = animation.getAnimatedValue("left") as Float
            mTransfrom!!.rect!!.top = animation.getAnimatedValue("top") as Float
            mTransfrom!!.rect!!.width = animation.getAnimatedValue("width") as Float
            mTransfrom!!.rect!!.height = animation.getAnimatedValue("height") as Float
            mBgAlpha = animation.getAnimatedValue("alpha") as Int
            invalidate()
            (context as Activity).window.decorView.invalidate()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                /*
				 * 如果是进入的话，当然是希望最后停留在center_crop的区域。但是如果是out的话，就不应该是center_crop的位置了
				 * ， 而应该是最后变化的位置，因为当out的时候结束时，不回复视图是Normal，要不然会有一个突然闪动回去的bug
				 */
                // TODO 这个可以根据实际需求来修改
                if (state == STATE_TRANSFORM_IN) {
                    mState = STATE_NORMAL
                }
                if (mTransformListener != null) {
                    mTransformListener!!.onTransformComplete(state)
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }
        })
        valueAnimator.start()
    }

    fun setOnTransformListener(listener: SmoothImageView.TransformListener) {
        mTransformListener = listener
    }

    private var mTransformListener: SmoothImageView.TransformListener? = null

    interface TransformListener {
        /**
         * @param mode STATE_TRANSFORM_IN 1 ,STATE_TRANSFORM_OUT 2
         */
        fun onTransformComplete(mode: Int) // mode 1
    }

    companion object {
        private val STATE_NORMAL = 0
        private val STATE_TRANSFORM_IN = 1
        private val STATE_TRANSFORM_OUT = 2

        /**
         * 获取状态栏高度

         * @return
         */
        fun getStatusBarHeight(context: Context): Int {
            var c: Class<*>? = null
            var obj: Any? = null
            var field: java.lang.reflect.Field? = null
            var x = 0
            var statusBarHeight = 0
            try {
                c = Class.forName("com.android.internal.R\$dimen")
                obj = c!!.newInstance()
                field = c.getField("status_bar_height")
                x = Integer.parseInt(field!!.get(obj).toString())
                statusBarHeight = context.resources.getDimensionPixelSize(x)
                return statusBarHeight
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return statusBarHeight
        }
    }

}

