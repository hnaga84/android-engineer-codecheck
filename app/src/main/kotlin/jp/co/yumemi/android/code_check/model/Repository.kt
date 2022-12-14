package jp.co.yumemi.android.code_check.model

import java.io.Serializable

data class Repository(
    val name: String = "",
    val full_name: String = "",
    val owner: Owner? = null,
    val language: String? = null,
    val description: String? = "",
    val updated_at: String = "",
    val stargazers_count: Long = 0,
    val watchers_count: Long = 0,
    val forks_count: Long = 0,
    val open_issues_count: Long = 0,
    val visibility: String = "",
    val html_url: String = ""
) : Serializable {
    init {

    }

    val stargazersCount: Long get() = stargazers_count
    val watchersCount: Long get() = watchers_count
    val forksCount: Long get() = forks_count
    val openIssuesCount: Long get() = open_issues_count
    val ownerIconUrl: String? get() = owner?.avatar_url
    val fullName: String get() = full_name
    val htmlUrl: String get() = html_url
    val updatedAt: String get() = updated_at

}


data class Owner(
    val avatar_url: String = ""
) : Serializable