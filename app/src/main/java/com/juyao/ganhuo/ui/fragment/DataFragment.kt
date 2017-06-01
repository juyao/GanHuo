package com.juyao.ganhuo.ui.fragment

import android.os.Bundle
import com.juyao.ganhuo.model.GanHuoBean
import com.juyao.ganhuo.present.PDataFragment
import kotlinx.android.synthetic.main.fragment_data.*

/**
 * Created by juyao on 2017/5/28 at 15:11.\n
 * 邮箱:juyao0909@gmail.com
 */


abstract class DataFragment : BaseFragment<PDataFragment>() {
    abstract fun getType():String
    val MAX_PAGESIZE:Int=20
    override fun newP(): PDataFragment {
        return PDataFragment()
    }
    abstract fun getDataResult(dataList:List<GanHuoBean>)

    fun refreshComplete(){
        data_list.refreshComplete()
        data_list.loadMoreComplete()
    }

}
