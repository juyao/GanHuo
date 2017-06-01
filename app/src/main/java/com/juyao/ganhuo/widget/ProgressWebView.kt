package com.juyao.ganhuo.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AbsoluteLayout
import android.widget.ImageView
import android.widget.ProgressBar

import cn.droidlover.xdroidmvp.log.XLog

class ProgressWebView(private val mContext: Context, attrs: AttributeSet) : WebView(mContext, attrs) {
    private val progressbar: ProgressBar
    private var mTitleChangeListener: TitleChangeListener? = null
    private val mImageView: ImageView? = null


    init {
        progressbar = ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal)
        progressbar.layoutParams = AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 5, 0, 0)
        addView(progressbar)
        setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //view.loadUrl(url);
                if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse(url))
                    context.startActivity(intent)
                    return true
                }
                return false
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed() // 接受证书
                XLog.d("网页错误：" + error.toString())
            }
        })
        setWebChromeClient(WebChromeClient())
        // 设置WebView属性，能够执行Javascript脚本
        val webSettings = settings
        webSettings.saveFormData = true
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(false)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.blockNetworkImage = false
        //设置 缓存模式
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        // 开启 DOM storage API 功能
        webSettings.domStorageEnabled = true
        //允许混合协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress == 100) {
                progressbar.visibility = View.GONE
                //=====================以下是测试代码=========================
                //				if(mImageView!=null)
                //					mImageView.setVisibility(View.GONE);
                //				Animation translate_in= AnimationUtils.loadAnimation(mContext, R.anim.translate_in); translate_in.setFillAfter(true);
                //				translate_in.setDuration(1000);
                //				translate_in.setDetachWallpaper(true);
                //				// translate_in.
                //				view.setAnimation(translate_in);
                //				Animation translate_out=AnimationUtils.loadAnimation(mContext, R.anim.translate_out);
                //				translate_out.setAnimationListener(new Animation.AnimationListener(){
                //					@Override
                //					public void onAnimationEnd(Animation animation) {
                //						if(null!=mImageView){
                //							removeView(mImageView);
                //							mImageView=null;
                //						}
                //
                //					}
                //					@Override
                //					public void onAnimationRepeat(Animation animation) {
                //						// TODO Auto-generated method stub
                //
                //					}
                //					@Override
                //					public void onAnimationStart(Animation animation) {
                //						// TODO Auto-generated method stub
                //
                //					}
                //
                //				});
                //				translate_out.setFillAfter(true);
                //				translate_out.setDuration(1000);
                //				translate_out.setDetachWallpaper(true);
                //				if(null!=mImageView){
                //					mImageView.setAnimation(translate_out);
                //				}
                //==============================================================================


            } else {
                if (progressbar.visibility == View.GONE)
                    progressbar.visibility = View.VISIBLE
                progressbar.progress = newProgress
                //===========================================================================
                //				if(mImageView==null){
                //					mImageView=new ImageView(mContext);
                //					view.setDrawingCacheEnabled(true);
                //					Bitmap bitmap=view.getDrawingCache();
                //					if(null!=bitmap){
                //						Bitmap b= Bitmap.createBitmap(bitmap);
                //						mImageView.setImageBitmap(b);
                //					}
                //					addView(mImageView);
                //				}
                //===========================================================================
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView, title: String?) {
            super.onReceivedTitle(view, title)
            if (title != null && mTitleChangeListener != null) {
                mTitleChangeListener!!.receiveTitle(title)
            }


        }


    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val lp = progressbar.layoutParams as AbsoluteLayout.LayoutParams
        lp.x = l
        lp.y = t
        progressbar.layoutParams = lp
        super.onScrollChanged(l, t, oldl, oldt)
    }

    fun setOnTitleChangeListener(titleChangeListener: TitleChangeListener) {
        this.mTitleChangeListener = titleChangeListener
    }

    interface TitleChangeListener {
        fun receiveTitle(title: String)
    }
}
