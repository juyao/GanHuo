package com.juyao.ganhuo.model

/**
 * Created by juyao on 2017/5/31 at 11:16.\n
 * 邮箱:juyao0909@gmail.com
 */


class HttpResult<T> : BaseModel() {
    var isError: Boolean = false
    var results: T? = null
}
