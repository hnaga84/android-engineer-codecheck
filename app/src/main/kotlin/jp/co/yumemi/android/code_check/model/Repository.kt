package jp.co.yumemi.android.code_check.model

import java.io.Serializable

data class Repository(
    val name: String = "",
    val ownerIconUrl: String? = null,
    val language: String = "",
    val stargazersCount: Long = 0,
    val watchersCount: Long = 0,
    val forksCount: Long = 0,
    val openIssuesCount: Long = 0
) : Serializable