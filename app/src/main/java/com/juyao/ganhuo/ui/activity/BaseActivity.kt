package com.juyao.ganhuo.ui.activity

import android.graphics.Color
import android.os.Bundle
import cn.droidlover.xdroidmvp.mvp.IPresent
import cn.droidlover.xdroidmvp.mvp.XActivity
import com.juyao.ganhuo.R
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by juyao on 2017/5/27 at 14:43.\n
 * 邮箱:juyao0909@gmail.com
 */


 abstract class BaseActivity<P : IPresent<*>> : XActivity<P>(){
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
     if(toolbar!=null){
         toolbar.setNavigationIcon(R.drawable.ic_action_back)
         toolbar.setTitle("")
         toolbar.setTitleTextColor(Color.WHITE)
         setSupportActionBar(toolbar)
         toolbar.setNavigationOnClickListener { navigateClick() }
     }
    initData(savedInstanceState)
 }
    abstract fun navigateClick()

}
