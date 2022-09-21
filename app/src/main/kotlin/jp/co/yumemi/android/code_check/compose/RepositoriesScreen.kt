package jp.co.yumemi.android.code_check.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Repository


sealed class RepositoriesViewEvent {
    data class Search(val inputText: String) : RepositoriesViewEvent()
    data class ShowRepository(val repository: Repository) : RepositoriesViewEvent()
}


@ExperimentalMaterialApi
@Composable
fun RepositoriesScreen(
    repositories: LiveData<List<Repository>>,
    onEvent: (RepositoriesViewEvent) -> Unit
) {
    val inputText = remember { mutableStateOf("") }

    Column {
        Row {

            Image(
                painter = rememberVectorPainter(Icons.Default.Search),
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )

            TextField(
                value = inputText.value,
                onValueChange = {
                    inputText.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    onEvent(RepositoriesViewEvent.Search(inputText = inputText.value))
                }),
                placeholder = { Text(text = stringResource(R.string.searchInputText_hint)) },
                modifier = Modifier.weight(1f)
            )

        }
        LazyColumn {
            items(repositories.value ?: emptyList()) { repository ->
                Text(repository.name)
            }
        }
    }
}
