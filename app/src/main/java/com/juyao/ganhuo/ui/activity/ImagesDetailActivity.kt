package com.juyao.ganhuo.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager

import com.juyao.ganhuo.R
import com.juyao.ganhuo.ui.present.PMain
import com.juyao.ganhuo.widget.SmoothImageView

import uk.co.senab.photoview.PhotoViewAttacher

import cn.droidlover.xdroidmvp.imageloader.ILFactory

/**
 * 描述：图片详情展示

 * @author hp
 * *
 * @time 2016/10/26 0026 15:26
 */
class ImagesDetailActivity : BaseActivity<PMain>() {

    private var mImageUrl: String? = null
    private var mLocationX: Int = 0
    private var mLocationY: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mSmoothImageView: SmoothImageView? = null


    private fun getBundleExtras(extras: Bundle) {
        mImageUrl = extras.getString(INTENT_IMAGE_URL_TAG)
        mLocationX = extras.getInt(INTENT_IMAGE_X_TAG)
        mLocationY = extras.getInt(INTENT_IMAGE_Y_TAG)
        mWidth = extras.getInt(INTENT_IMAGE_W_TAG)
        mHeight = extras.getInt(INTENT_IMAGE_H_TAG)
    }


    override fun onBackPressed() {
        mSmoothImageView!!.transformOut()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
    }


    private fun initViewsAndEvents() {
        mSmoothImageView!!.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY)
        mSmoothImageView!!.transformIn()
        ILFactory.getLoader().loadNet(mSmoothImageView, mImageUrl, null)
        //        Picasso.with(this)
        //                .load(mImageUrl)
        //                .placeholder(R.mipmap.ic_launcher)
        //                .error(R.mipmap.ic_launcher)
        //                .into(mSmoothImageView);

        mSmoothImageView!!.setOnTransformListener { mode ->
            if (mode == 2) {
                finish()
            }
        }
        mSmoothImageView!!.setOnPhotoTapListener { view, v, v2 -> mSmoothImageView!!.transformOut() }
    }

    /**
     * set status bar translucency

     * @param on
     */
    private fun setTranslucentStatus(on: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val win = window
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    override fun navigateClick() {

    }

    override fun initData(bundle: Bundle) {
        mSmoothImageView = findViewById(R.id.images_detail_smooth_image) as SmoothImageView

        val extras = intent.extras
        if (null != extras) {
            getBundleExtras(extras)
        }
        setTranslucentStatus(true)

        initViewsAndEvents()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_images_detail
    }

    override fun newP(): PMain {
        return PMain()
    }

    companion object {

        val INTENT_IMAGE_URL_TAG = "INTENT_IMAGE_URL_TAG"
        val INTENT_IMAGE_X_TAG = "INTENT_IMAGE_X_TAG"
        val INTENT_IMAGE_Y_TAG = "INTENT_IMAGE_Y_TAG"
        val INTENT_IMAGE_W_TAG = "INTENT_IMAGE_W_TAG"
        val INTENT_IMAGE_H_TAG = "INTENT_IMAGE_H_TAG"
    }
}
