/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.Repository
import jp.co.yumemi.android.code_check.net.GithubAppRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.*

/**
 * RepositoriesFragment で使う
 */
class RepositoriesViewModel(
    private val githubAppRepository: GithubAppRepository = GithubAppRepository()
) : ViewModel(
) {
    var repositories: MutableLiveData<List<Repository>> = MutableLiveData<List<Repository>>()
    val searchError: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    var alertTitle: String? = null
    var alertMessage: String? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is HttpException -> {
                    alertTitle = "通信エラー"
                    alertMessage = "時間をおいてから再度お試しください"
                    searchError.postValue(throwable)
                }
                is UnknownHostException -> {
                    alertTitle = "通信エラー"
                    alertMessage = "インターネットの接続を確認してください"
                    searchError.postValue(throwable)
                }
                else -> {
                    Log.d("error", throwable.printStackTrace().toString())
                }
            }
        }

    private fun fetchRepositories(inputText: String) {
        scope.launch(coroutineExceptionHandler) {
            repositories.postValue(githubAppRepository.getRepositories(query = inputText)?.items)
            lastSearchDate = Date()
        }
    }

    fun search(inputText: String) {
        if (inputText.isNotEmpty()) {
            fetchRepositories(inputText)
        }
    }

}