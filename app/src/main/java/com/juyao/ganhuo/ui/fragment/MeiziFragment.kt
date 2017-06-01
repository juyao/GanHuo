package com.juyao.ganhuo.ui.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import cn.droidlover.xdroidmvp.base.RecyclerItemCallback
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.juyao.ganhuo.R
import com.juyao.ganhuo.adapter.MeiZiAdapter
import com.juyao.ganhuo.model.GanHuoBean
import com.juyao.ganhuo.tools.SpacesItemDecoration
import com.juyao.ganhuo.ui.activity.ImagesDetailActivity
import kotlinx.android.synthetic.main.fragment_data.*


/**
 *
 *
 *Created by juyao on 2017/5/31 at 16:25.\n
 * 邮箱:juyao0909@gmail.com
 */
class MeiziFragment: DataFragment() {
    var mMeiziAdapter: MeiZiAdapter?=null
    var pageNum=1
    override fun getType(): String {
        return "福利"
    }

    override fun initData(p0: Bundle?) {
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        data_list.setLayoutManager(layoutManager)
        mMeiziAdapter = MeiZiAdapter(context)
        data_list.adapter= mMeiziAdapter
        data_list.setPadding(8,8,8,8)
        data_list.addItemDecoration(SpacesItemDecoration(8))
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
        mMeiziAdapter!!.recItemClick = object : RecyclerItemCallback<GanHuoBean, MeiZiAdapter.ViewHolder>() {
            override fun onItemClick(position: Int, model: GanHuoBean, tag: Int, holder: MeiZiAdapter.ViewHolder?) {
                super.onItemClick(position, model, tag, holder)
                val frame = Rect()
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame)
                val statusBarHeight = frame.top
                val location = IntArray(2)
                holder!!.itemView.getLocationOnScreen(location)
                location[1] += statusBarHeight
                val width = holder!!.itemView.getWidth()
                val height = holder!!.itemView.getHeight()
                navigateToImagesDetail(position,
                        model,
                        location[0],
                        location[1],
                        width,
                        height)

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
        fun getInstance():MeiziFragment{
            return  MeiziFragment()
        }
    }

    override fun getDataResult(dataList: List<GanHuoBean>) {

        if(pageNum==2){
            mMeiziAdapter!!.setData(dataList)
        }else{
            mMeiziAdapter!!.addData(dataList)
        }

    }

    override fun onDestroy() {
        //transferee.destroy()
        super.onDestroy()

    }

    private fun navigateToImagesDetail(position: Int, entity: GanHuoBean, x: Int, y: Int, width: Int, height: Int) {
        val extras = Bundle()
        extras.putString(ImagesDetailActivity.INTENT_IMAGE_URL_TAG, entity.url)
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_X_TAG, x)
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_Y_TAG, y)
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_W_TAG, width)
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_H_TAG, height)
        readyGo(ImagesDetailActivity::class.java, extras)
        activity.overridePendingTransition(0, 0)
    }

    private fun readyGo(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(getContext(), clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }




}

