package com.juyao.ganhuo.ui.fragment

import android.os.Bundle

import cn.droidlover.xdroidmvp.mvp.IPresent
import cn.droidlover.xdroidmvp.mvp.XFragment

/**
 * Created by juyao on 2017/5/28 at 15:12.\n
 * 邮箱:juyao0909@gmail.com
 */


abstract class BaseFragment<P : IPresent<*>> : XFragment<P>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



}
