package com.bgct.pagehelper

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

typealias VmFlow<T> = Flow<VmResult<T>>
typealias VmStateFlow<T> = StateFlow<VmResult<T>>
typealias VmChannel<T> = Channel<VmResult<T>>

sealed class VmResult<T> {
    class Succeed<T>(
        val data: T,
        val extra: VmExtra = VmExtra()
    ) : VmResult<T>()

    class Error<T>(val t: Throwable, val isLoadMore: Boolean = false) : VmResult<T>()
    class Loading<T>(val msg: String = "", val isLoadMore: Boolean = false) : VmResult<T>()
}

data class VmExtra(
    val status: LoadMoreStatus = LoadMoreStatus.Gone,
    val opt: LoadMoreOpt = LoadMoreOpt.AllData(),
    val source: Source = Source.Network
)

sealed class Source {
    object Network : Source()
    object Local : Source()
    object Memory : Source()
}

sealed class LoadMoreStatus {
    class NoMore(val msg: String = "") : LoadMoreStatus()
    object Gone : LoadMoreStatus()
}

sealed class LoadMoreOpt {
    //头部插入数据
    object Append : LoadMoreOpt()

    //尾部插入数据
    object Prepend : LoadMoreOpt()

    //全量数据
    class AllData(val refresh: Boolean = false) : LoadMoreOpt()
}

class VmResultListener<T>(val res: VmResult<T>) {
    var onSucceed: ((T) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
    var onLoad: ((String) -> Unit)? = null
}

fun <T> VmResult<T>.listener(callback: VmResultListener<T>.() -> Unit) {
    val listener = VmResultListener(this)
    callback.invoke(listener)
    val res = this
    if (res is VmResult.Succeed && res.extra.status == LoadMoreStatus.Gone) {
        listener.onSucceed?.invoke(res.data)
    } else if (res is VmResult.Error && !res.isLoadMore) {
        listener.onError?.invoke(res.t)
    } else if (res is VmResult.Loading && !res.isLoadMore) {
        listener.onLoad?.invoke(res.msg)
    }
}

fun <T> VmResult<T>.convertLoadStatus(): LoadStatus {
    val res = this
    return when (res) {
        is VmResult.Error -> {
            if (res.isLoadMore) {
                LoadStatus.LoadMoreError(res.t)
            } else {
                LoadStatus.Error(res.t)
            }
        }

        is VmResult.Loading -> {
            if (res.isLoadMore) {
                LoadStatus.LoadMoreLoading(res.msg)
            } else {
                LoadStatus.Loading(res.msg)
            }
        }

        is VmResult.Succeed -> loadMore2LoadStatus(res.extra.status)
    }
}

fun <T> VmResult<T>.isLoading(): Boolean {
    val res = this
    return res is VmResult.Loading && !res.isLoadMore
}

fun <T> VmResult<T>.isError(): Boolean {
    val res = this
    return res is VmResult.Error && !res.isLoadMore
}

fun <T> VmResult<T>.isLoadMore(): Boolean {
    return when (val res = this) {
        is VmResult.Loading -> {
            res.isLoadMore
        }

        is VmResult.Error -> {
            res.isLoadMore
        }

        is VmResult.Succeed -> {
            res.extra.status is LoadMoreStatus.NoMore
        }
    }
}

fun <T> VmResult<T>.isSucceed(): Boolean {
    val res = this
    return res is VmResult.Succeed && res.extra.status is LoadMoreStatus.Gone
}

private fun loadMore2LoadStatus(status: LoadMoreStatus): LoadStatus {
    return when (status) {
        is LoadMoreStatus.NoMore -> LoadStatus.LoadMoreNoMore(status.msg)
        else -> LoadStatus.Gone
    }
}
