package com.juyao.ganhuo.tools

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

import java.util.UUID

import cn.droidlover.xdroidmvp.log.XLog

/**
 * Created by juyao on 2017/2/21 at 20:49.\n 邮箱:juyao0909@gmail.com
 */


object DeviceUtils {
    /**
     * 获取屏幕分辨率

     * @param context
     * *
     * @return
     */

    fun getScreenDispaly(context: Context): IntArray {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width// 手机屏幕的宽度
        val height = windowManager.defaultDisplay.height// 手机屏幕的高度
        val result = intArrayOf(width, height)
        return result
    }

    /**
     * 获取手机唯一序列号
     */
    fun getDeviceId(context: Context): String {
        var id = ""
        try {
            id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            id = UUID.randomUUID().toString()
        }

        return id
    }

    private val TAG = "YanZi"
    /**
     * dipתpx
     * @param context
     * *
     * @param dipValue
     * *
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * pxתdip
     * @param context
     * *
     * @param pxValue
     * *
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


    fun getScreenMetrics(context: Context): Point {
        val dm = context.resources.displayMetrics
        val w_screen = dm.widthPixels
        val h_screen = dm.heightPixels
        Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi)
        return Point(w_screen, h_screen)

    }


    fun getScreenRate(context: Context): Float {
        val P = getScreenMetrics(context)
        val H = P.y.toFloat()
        val W = P.x.toFloat()
        return H / W
    }

    /**
     * 手机屏幕的宽度
     * @param context
     * *
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width// 手机屏幕的宽度
        return width
    }

    /**
     * 手机屏幕的高度
     * @param context
     * *
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = windowManager.defaultDisplay.height// 手机屏幕的高度
        return height
    }

    /**
     * 返回当前程序版本名
     */
    fun getAppVersionName(context: Context): String? {
        var versionName: String? = ""
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionName = pi.versionName
            //int versioncode = pi.versionCode;
            if (versionName == null || versionName.length <= 0) {
                return ""
            }
        } catch (e: Exception) {
            XLog.e("getAppVersionName error", "Exception", e)
        }

        return versionName
    }

    /**
     * 获取程序版本号
     */
    fun getAppVersionCode(context: Context): Int {
        var versionCode = 0
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionCode = pi.versionCode
            //int versioncode = pi.versionCode;
        } catch (e: Exception) {
            XLog.e("getAppVersionName error", "Exception", e)
        }

        return versionCode
    }

    /**
     * 将sp值转换为px值，保证文字大小不变

     * @param spValue
     * *
     * @param
     * *            （DisplayMetrics类中属性scaledDensity）
     * *
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

}
