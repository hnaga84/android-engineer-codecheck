package jp.co.yumemi.android.code_check.net;

import jp.co.yumemi.android.code_check.model.MyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApiInterface {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("search/repositories")
    fun getRepositories(@Query("q") query: String?): Call<MyResponse>

}
