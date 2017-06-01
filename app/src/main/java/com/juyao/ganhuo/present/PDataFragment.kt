package com.juyao.ganhuo.present

import com.juyao.ganhuo.model.GanHuoBean
import com.juyao.ganhuo.net.Api
import com.juyao.ganhuo.ui.fragment.DataFragment
import com.juyao.ganhuo.ui.present.PBaseActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 *
 *
 *Created by juyao on 2017/5/28 at 15:18.\n
 * 邮箱:juyao0909@gmail.com
 */
class PDataFragment: PBaseActivity<DataFragment>() {
    /**
     * 获取干货数据
     */
    fun getGanHuoData(type: String, pageSize: Int, pageNum: Int) {
        Api.ganHuoService!!.getGanHuoData(type, pageSize, pageNum)
                .map<List<GanHuoBean>>(HttpResultFunc<List<GanHuoBean>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<GanHuoBean>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if(v!=null){
                            v.refreshComplete()
                        }
                    }

                    override fun onNext(ganHuoBeens: List<GanHuoBean>) {
                        if(v!=null){
                            v.refreshComplete()
                            v.getDataResult(ganHuoBeens)
                        }
                    }
                })
    }
}

