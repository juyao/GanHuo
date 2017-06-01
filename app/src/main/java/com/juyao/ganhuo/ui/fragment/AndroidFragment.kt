package com.juyao.ganhuo.ui.fragment

import android.content.Intent
import android.os.Bundle
import cn.droidlover.xdroidmvp.log.XLog
import com.juyao.ganhuo.R
import com.juyao.ganhuo.adapter.CommonAdapter
import com.juyao.ganhuo.model.GanHuoBean
import kotlinx.android.synthetic.main.fragment_data.*
import android.support.v7.widget.LinearLayoutManager
import cn.droidlover.xdroidmvp.base.RecyclerItemCallback
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.juyao.ganhuo.ui.activity.CommonWebActivity


/**
 *
 *
 *Created by juyao on 2017/5/31 at 13:23.\n
 * 邮箱:juyao0909@gmail.com
 */
class AndroidFragment: DataFragment() {
    var mCommonAdapter: CommonAdapter?=null
    var pageNum=1
    override fun getType(): String {
        return "Android"
    }

    override fun initData(p0: Bundle?) {

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        data_list.setLayoutManager(layoutManager)
        mCommonAdapter = CommonAdapter(context)
        data_list.adapter= mCommonAdapter
        loadData(pageNum++)
        data_list!!.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                pageNum=1
                loadData(pageNum++)

            }

            override fun onLoadMore() {
                loadData(pageNum++)
            }
        })
        mCommonAdapter!!.recItemClick = object : RecyclerItemCallback<GanHuoBean, CommonAdapter.ViewHolder>() {
            override fun onItemClick(position: Int, model: GanHuoBean?, tag: Int, holder: CommonAdapter.ViewHolder?) {
                super.onItemClick(position, model, tag, holder)
                val intent = Intent(getContext(),CommonWebActivity::class.java)
                intent.putExtra("url",model!!.url)
                startActivity(intent)
            }
        }
    }
    fun loadData(page:Int){
        p.getGanHuoData(getType(),MAX_PAGESIZE,page)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_data
    }

    companion object{
        fun getInstance():AndroidFragment{
            return  AndroidFragment()
        }
    }

    override fun getDataResult(dataList: List<GanHuoBean>) {
        XLog.d("获取到了干货数据，数据条数："+dataList.size)
        if(pageNum==2){
            mCommonAdapter!!.setData(dataList)
        }else{
            mCommonAdapter!!.addData(dataList)
        }

    }




}


