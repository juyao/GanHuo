package com.juyao.ganhuo.model

import cn.droidlover.xdroidmvp.net.IModel


/**
 * Created by juyao on 2017/1/12 at 14:00. 邮箱:juyao0909@gmail.com .--,       .--, ( (  \.---./  ) )
 * '.__/o   o\__.' {=  ^  =} >  -  < /       \ //       \\ //|   .   |\\ "'\       /'"_.-~^`'-. \  _
 * /--'         ` ___)( )(___ (((__) (__)))    高山仰止,景行行止.虽不能至,心向往之。
 */


open class BaseModel : IModel {
    override fun isNull(): Boolean {
        return false
    }

    override fun isAuthError(): Boolean {
        return false
    }

    override fun isBizError(): Boolean {
        return false
    }
}
