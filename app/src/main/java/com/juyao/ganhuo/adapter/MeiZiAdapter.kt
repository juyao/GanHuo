package com.juyao.ganhuo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter
import cn.droidlover.xdroidmvp.imageloader.ILFactory
import com.juyao.ganhuo.R
import com.juyao.ganhuo.model.GanHuoBean
import com.juyao.ganhuo.tools.DeviceUtils
import java.util.*


/**
 *
 *
 *Created by juyao on 2017/5/31 at 16:59.\n
 * 邮箱:juyao0909@gmail.com
 */
class MeiZiAdapter(context: Context) : SimpleRecAdapter<GanHuoBean, MeiZiAdapter.ViewHolder>(context) {
    var itemWith:Int = 0
    init {
      itemWith=(DeviceUtils.getScreenWidth(context)-32)/2
    }
    override fun newViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun getLayoutId(): Int {
        return R.layout.imageitem_meizi
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val ganhuo = data[i]
        val layoutParams = viewHolder.img_meizi.getLayoutParams()
        val random = Random()
        val max=15
        val min=10
        val s = random.nextInt(max) % (max - min + 1) + min
        layoutParams.width = itemWith
        layoutParams.height = (itemWith*(s/10.0)).toInt()
        viewHolder.img_meizi.setLayoutParams(layoutParams)
        viewHolder.titleText.text = ganhuo.desc
        if(TextUtils.isEmpty(ganhuo.who)){
            viewHolder.authorText.text="from.佚名"
        }else{
            viewHolder.authorText.text ="from."+ganhuo.who
        }
        ILFactory.getLoader().loadNet(viewHolder.img_meizi, ganhuo.url, null)
        //Glide.with(context).load(ganhuo.url).transform(GlideRoundTransform(context,10)).into(viewHolder.img_meizi)
        viewHolder.itemView.setOnClickListener {
            if (recItemClick != null) {
                recItemClick.onItemClick(i, ganhuo, 0, viewHolder)
            }

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var img_meizi: ImageView
        internal var titleText: TextView
        internal var authorText: TextView
        init {
            titleText = itemView.findViewById(R.id.text_imgdec) as TextView
            authorText = itemView.findViewById(R.id.text_author) as TextView
            img_meizi=itemView.findViewById(R.id.img_meizi) as ImageView
        }
    }
}




