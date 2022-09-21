package jp.co.yumemi.android.code_check.net

import android.util.Log
import jp.co.yumemi.android.code_check.model.MyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class GithubAppRepository() {
    private var service: GithubApiInterface = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApiInterface::class.java)

    fun getRepositories(query: String?): MyResponse? {
        try {
            val response = service.getRepositories(query).execute()

            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.d("GithubAppRepository", "GET ERROR")
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}