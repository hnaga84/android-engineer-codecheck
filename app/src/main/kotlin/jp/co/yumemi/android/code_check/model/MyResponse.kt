package jp.co.yumemi.android.code_check.model

import java.io.Serializable

data class MyResponse(
    val items: List<Repository> = emptyList()
) : Serializable {
    init {

    }
}