package com.bgct.pagehelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bgct.uicore.LoadingView
import kotlinx.coroutines.launch

sealed class LoadStatus {
    class Error(val t: Throwable) : LoadStatus()
    class Loading(val msg: String = "") : LoadStatus()
    class LoadMoreNoMore(val msg: String = "") : LoadStatus()
    class LoadMoreLoading(val msg: String = "") : LoadStatus()
    class LoadMoreError(val t: Throwable) : LoadStatus()
    object Gone : LoadStatus()

    fun isShowLoadMore(): Boolean {
        return this is LoadMoreLoading ||
                this is LoadMoreNoMore ||
                this is LoadMoreError
    }

    fun isShowStatus(): Boolean {
        return this is Loading || this is Error
    }

    fun isGone(): Boolean {
        return this is Gone
    }
}

fun LoadStatus.isShowDefault(): Boolean {
    return !isGone()
}

@Composable
fun CreateLoadStatusUi(
    loadStatus: LoadStatus,
    onRefresh: (suspend () -> Unit),
    onLoadMore: suspend () -> Unit,
    showStatus: () -> Boolean = { loadStatus.isShowDefault() },
    loadError: @Composable BoxScope.() -> Unit = { LoadErrorDefault(onRefresh = onRefresh) },
    loading: @Composable BoxScope.() -> Unit = {},
    loadMoreLoading: @Composable BoxScope.() -> Unit = { LoadMoreLoadingDefault() },
    loadMoreNoMore: @Composable BoxScope.() -> Unit = { LoadMoreNoMoreDefault() },
    loadMoreError: @Composable BoxScope.() -> Unit = { LoadMoreErrorDefault(onLoadMore = onLoadMore) },
) {
    if (showStatus.invoke()) {
        Box {
            when (loadStatus) {
                is LoadStatus.Loading -> {
                    loading()
                }

                is LoadStatus.LoadMoreLoading -> {
                    loadMoreLoading()
                }

                is LoadStatus.LoadMoreNoMore -> {
                    loadMoreNoMore()
                }

                is LoadStatus.LoadMoreError -> {
                    loadMoreError()
                }

                is LoadStatus.Error -> {
                    loadError()
                }

                else -> {

                }
            }
        }
    }
}

@Composable
fun LoadErrorDefault(
    modifier: Modifier = Modifier.fillMaxSize(),
    msg: String = stringResource(id = R.string.load_error),
    onRefresh: (suspend () -> Unit)
) {
    Box(modifier = modifier) {
        val scope = rememberCoroutineScope()
        Button(onClick = {
            scope.launch {
                onRefresh.invoke()
            }
        }, modifier = Modifier.align(Alignment.Center)) {
            Text(text = msg)
        }
    }
}

@Composable
fun LoadMoreErrorDefault(
    modifier: Modifier = Modifier.fillMaxWidth(),
    msg: String = stringResource(id = R.string.load_error),
    onLoadMore: (suspend () -> Unit)
) {
    Box(modifier = modifier) {
        val scope = rememberCoroutineScope()
        Button(onClick = {
            scope.launch {
                onLoadMore.invoke()
            }
        }, modifier = Modifier.align(Alignment.Center)) {
            Text(text = msg)
        }
    }
}

@Composable
fun LoadMoreNoMoreDefault(
    modifier: Modifier = Modifier.fillMaxWidth(),
    msg: String = stringResource(id = R.string.load_more_no_more),
) {
    Box(modifier = modifier) {
        Text(
            text = msg,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun LoadMoreLoadingDefault(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp),
    msg: String = stringResource(id = R.string.load_more_loading),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LoadingView(isRotating = true)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            msg,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp
        )
    }
}




