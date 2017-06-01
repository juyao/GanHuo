package com.juyao.ganhuo.ui.present

import cn.droidlover.xdroidmvp.log.XLog
import cn.droidlover.xdroidmvp.mvp.IView
import cn.droidlover.xdroidmvp.mvp.XPresent
import com.juyao.ganhuo.model.HttpResult
import rx.functions.Func1


/**
 *
 *
 *Created by juyao on 2017/5/27 at 14:54.\n
 * 邮箱:juyao0909@gmail.com
 */
abstract class PBaseActivity<V:IView<*>>: XPresent<V>() {
    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber

     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
    </T> */
    inner class HttpResultFunc<T> : Func1<HttpResult<T>, T> {

        override fun call(httpResult: HttpResult<T>): T? {
            XLog.d("请求结果返回：" + httpResult.toString())
            if (!httpResult.isError) {
                return httpResult.results
            }
            return null
        }
    }
}


