package com.juyao.ganhuo.net

import com.juyao.ganhuo.model.GanHuoBean
import com.juyao.ganhuo.model.HttpResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by juyao on 2017/5/28 at 14:53.\n
 * 邮箱:juyao0909@gmail.com
 */


interface GanHuoService{
    @GET("data/{type}/{number}/{page}")
    fun getGanHuoData(@Path("type") type: String,
                  @Path("number") pageSize: Int,
                  @Path("page") pageNum: Int): Observable<HttpResult<List<GanHuoBean>>>

}
