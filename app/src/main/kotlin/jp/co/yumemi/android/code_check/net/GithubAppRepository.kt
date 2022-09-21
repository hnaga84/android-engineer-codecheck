package jp.co.yumemi.android.code_check.net

import jp.co.yumemi.android.code_check.model.MyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubAppRepository() {
    private var service: GithubApiInterface = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApiInterface::class.java)

    fun getRepositories(query: String?): MyResponse? {
        val response = service.getRepositories(query).execute()
        return response.body()
    }
}