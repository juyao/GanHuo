package com.juyao.ganhuo.net

import cn.droidlover.xdroidmvp.net.XApi

/**
 * Created by juyao on 2017/5/28 at 14:51.\n
 * 邮箱:juyao0909@gmail.com
 */


object Api {
    var API_BASE_URL = "http://gank.io/api/"
    private var sGanHuoService: GanHuoService? = null
    val ganHuoService: GanHuoService?
        get() {
            if (sGanHuoService == null) {
                synchronized(Api::class.java) {
                    if (sGanHuoService == null) {
                        sGanHuoService = XApi.getInstance().getRetrofit(true).create(GanHuoService::class.java)
                    }
                }

            }
            return sGanHuoService
        }
}
