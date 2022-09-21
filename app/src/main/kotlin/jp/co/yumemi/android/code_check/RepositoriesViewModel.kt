/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.Repository
import jp.co.yumemi.android.code_check.net.GithubAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * RepositoriesFragment で使う
 */
class RepositoriesViewModel : ViewModel(

) {
    var repositories: MutableLiveData<List<Repository>> = MutableLiveData<List<Repository>>()
    private val githubAppRepository: GithubAppRepository = GithubAppRepository()

    fun searchRepositories(inputText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositories.postValue(githubAppRepository.getRepositories(query = inputText)?.items)
            lastSearchDate = Date()
        }
    }

}