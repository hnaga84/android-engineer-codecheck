package jp.co.yumemi.android.code_check.compose

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import jp.co.yumemi.android.code_check.model.Repository

data class RepositoriesViewState(
    val repositories: List<Repository> = emptyList()
) {
    companion object {
        val Empty = RepositoriesViewState()
    }
}

sealed class RepositoriesViewEvent {
    data class ShowSubscription(val repository : Repository) : RepositoriesViewEvent()
}

@ExperimentalMaterialApi
@Composable
fun RepositoriesScreen(
    viewState: State<RepositoriesViewState>,
    onEvent: (RepositoriesViewEvent) -> Unit,
) {
    
}