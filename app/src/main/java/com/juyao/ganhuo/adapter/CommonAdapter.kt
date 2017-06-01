package com.juyao.ganhuo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.TextView

import com.juyao.ganhuo.R
import com.juyao.ganhuo.model.GanHuoBean

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter

/**
 * Created by juyao on 2017/5/31 at 12:44.\n
 * 邮箱:juyao0909@gmail.com
 */


class CommonAdapter(context: Context) : SimpleRecAdapter<GanHuoBean, CommonAdapter.ViewHolder>(context) {

    override fun newViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun getLayoutId(): Int {
        return R.layout.listitem_ganhuo
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val ganhuo = data[i]
        viewHolder.titleText.text = ganhuo.desc
        if(TextUtils.isEmpty(ganhuo.who)){
            viewHolder.authorText.text="佚名"
        }else{
            viewHolder.authorText.text =ganhuo.who
        }

        viewHolder.itemView.setOnClickListener {
            if (recItemClick != null) {
                recItemClick.onItemClick(i, ganhuo, 0, viewHolder)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var titleText: TextView
        internal var authorText: TextView
        init {
            titleText = itemView.findViewById(R.id.text_title) as TextView
            authorText = itemView.findViewById(R.id.text_author) as TextView
        }
    }
}
