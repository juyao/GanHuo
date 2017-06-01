package com.juyao.ganhuo.ui.activity

import android.graphics.Color
import android.os.Bundle
import cn.droidlover.xdroidmvp.base.XFragmentAdapter
import com.juyao.ganhuo.R
import com.juyao.ganhuo.ui.fragment.*
import com.juyao.ganhuo.ui.present.PMain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by juyao on 2017/5/27 at 14:58.\n
 * 邮箱:juyao0909@gmail.com
 */


class MainActivity : BaseActivity<PMain>() {
    override fun navigateClick() {

    }

    val titles= arrayOf("Android","iOS","前端","拓展资源","瞎推荐","App","休息视频","福利")
    internal var fragmentList: List<DataFragment>? = null
    val androidFragment=AndroidFragment.getInstance()
    val iOSFragmnet=IosFragment.getInstance()
    val qdFragment=QdFragment.getInstance()
    val tzFragment= TzFragment.getInstance()
    val recommendFragment=RecommendFragment.getInstance()
    val appFragment=AppFragment.getInstance()
    val meiziFragment=MeiziFragment.getInstance()
    val vedioFragment=VedioFragment.getInstance()
    var fragmentAdapter:XFragmentAdapter? = null
    override fun initData(bundle: Bundle?) {
        toolbar.setNavigationIcon(null)
        toolbar.setTitle("干货集中营")
        fragmentList=ArrayList<DataFragment>()
        (fragmentList as ArrayList<DataFragment>).add(androidFragment)
        (fragmentList as ArrayList<DataFragment>).add(iOSFragmnet)
        (fragmentList as ArrayList<DataFragment>).add(qdFragment)
        (fragmentList as ArrayList<DataFragment>).add(tzFragment)
        (fragmentList as ArrayList<DataFragment>).add(recommendFragment)
        (fragmentList as ArrayList<DataFragment>).add(appFragment)
        (fragmentList as ArrayList<DataFragment>).add(vedioFragment)
        (fragmentList as ArrayList<DataFragment>).add(meiziFragment)
        fragmentAdapter= XFragmentAdapter(supportFragmentManager,fragmentList,titles)
        main_viewpager.adapter=fragmentAdapter
        main_tab.setupWithViewPager(main_viewpager)

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun newP(): PMain {
        return PMain()
    }
}
