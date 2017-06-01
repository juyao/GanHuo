package com.juyao.ganhuo.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import cn.droidlover.xdroidmvp.log.XLog

import com.juyao.ganhuo.R
import com.juyao.ganhuo.tools.DeviceUtils
import com.juyao.ganhuo.ui.present.PMain
import com.juyao.ganhuo.widget.ProgressWebView
import kotlinx.android.synthetic.main.activity_common_web.*
import kotlinx.android.synthetic.main.toolbar.*

class CommonWebActivity : BaseActivity<PMain>() {
    override fun navigateClick() {
        if(common_web.canGoBack()){
            common_web.goBack()
        }else{
            finish()
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_common_web
    }

    override fun initData(p0: Bundle?) {
        if(!TextUtils.isEmpty(intent.getStringExtra("url"))){
            common_web.loadUrl(intent.getStringExtra("url"))
        }
       common_web.setOnTitleChangeListener(object : ProgressWebView.TitleChangeListener {
           override fun receiveTitle(title: String) {
               text_title.text=title
           }
       })
    }

    override fun newP(): PMain {
       return PMain()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_web,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val menuId=item!!.itemId;
        when(menuId){
            R.id.copytoclip->DeviceUtils.copyToClipBoard(context,intent.getStringExtra("url"))
        }
        return true
    }



}
