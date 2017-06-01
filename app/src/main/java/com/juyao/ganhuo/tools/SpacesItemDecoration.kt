package com.juyao.ganhuo.tools

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by juyao on 2017/5/31 at 17:26.\n
 * 邮箱:juyao0909@gmail.com
 */


class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        //注释这两行是为了上下间距相同
        //        if(parent.getChildAdapterPosition(view)==0){
        outRect.top = space
        //        }
    }
}
