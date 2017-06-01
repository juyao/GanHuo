package com.juyao.ganhuo

import android.app.Application
import android.content.Context
import android.support.v7.widget.LinearLayoutManager

import com.juyao.ganhuo.net.Api

import cn.droidlover.xdroidmvp.net.NetError
import cn.droidlover.xdroidmvp.net.NetProvider
import cn.droidlover.xdroidmvp.net.RequestHandler
import cn.droidlover.xdroidmvp.net.XApi
import com.jcodecraeer.xrecyclerview.XRecyclerView
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient


/**
 * Created by juyao on 2017/5/31 at 13:59.\n
 * 邮箱:juyao0909@gmail.com
 */


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        XApi.registerProvider(object : NetProvider {
            override fun configBaseUrl(): String {
                return Api.API_BASE_URL
            }

            override fun configInterceptors(): Array<Interceptor?> {
                return arrayOfNulls(0)
            }

            override fun configHttps(builder: OkHttpClient.Builder) {

            }

            override fun configCookie(): CookieJar? {
                return null
            }

            override fun configHandler(): RequestHandler? {
                return null
            }

            override fun configConnectTimeoutMills(): Long {
                return 0
            }

            override fun configReadTimeoutMills(): Long {
                return 0
            }

            override fun configLogEnable(): Boolean {
                return true
            }

            override fun handleError(error: NetError): Boolean {
                return false
            }
        })
    }
}
