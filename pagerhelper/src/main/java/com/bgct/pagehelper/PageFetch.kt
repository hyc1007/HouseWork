package com.bgct.pagehelper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class PageFetch<Key, Input, ListValue, Value : ILoadList<ListValue>>(
    private val scope: CoroutineScope,
    val config: PageConfig,
    private val pageSrc: PageSource<Key, Input, ListValue, Value>,
    private val initKey: Key,
) {
    private var mPageData: PageSource.PageResult<Key, ListValue, Value>? = null
    private val pageList = PageList<ListValue, Value>()
    private val _flow = Channel<PageList<ListValue, Value>>()
    internal val flow = _flow.receiveAsFlow()
    internal var inputCreate: (() -> Input)? = null

    internal fun sendEvent(event: PageEvent) {
        scope.launch {
            when (event) {
                is PageEvent.Next -> fetchNext()
                is PageEvent.Prev -> fetchPrev()
                is PageEvent.Refresh -> refresh()
            }
        }
    }

    private suspend fun refresh() {
        mPageData = null
        val param = PageSource.PageParam(config.pageSize, initKey, inputCreate?.invoke())
        createFetchFlow(param, LoadMoreOpt.AllData(true)).collect {
            handleResult(it)
        }
    }

    private suspend fun handleResult(res: VmPageResult<Key, ListValue, Value>) {
        when (res) {
            is VmResult.Succeed -> {
                mPageData = res.data
                pageList.addResult(VmResult.Succeed(res.data.data, res.extra))
                _flow.send(pageList.copy())
            }

            is VmResult.Error -> {
                pageList.addResult(VmResult.Error(res.t, res.isLoadMore))
                _flow.send(pageList.copy())
            }

            is VmResult.Loading -> {
                pageList.addResult(VmResult.Loading(res.msg, res.isLoadMore))
                _flow.send(pageList.copy())
            }
        }
    }


    private suspend fun fetchPrev() {
        val nextKey = mPageData?.prevKey
        nextKey?.apply {
            val param = PageSource.PageParam(config.pageSize, this as Key, inputCreate?.invoke())
            createFetchFlow(param, LoadMoreOpt.Prepend).collect {
                handleResult(it)
            }
        }
    }

    private suspend fun fetchNext() {
        val nextKey = mPageData?.nextKey
        nextKey?.apply {
            val param = PageSource.PageParam(config.pageSize, this as Key, inputCreate?.invoke())
            createFetchFlow(param, LoadMoreOpt.Append).collect {
                handleResult(it)
            }
        }
    }

    private fun createFetchFlow(
        param: PageSource.PageParam<Key, Input>,
        opt: LoadMoreOpt
    ): Flow<VmPageResult<Key, ListValue, Value>> {
        val loadMore = initKey != param.key
        return flow {
            emit(pageSrc.load(param))
        }.map {
            val nextKey = it.nextKey
            val noMore = nextKey == null
            val status =
                if (loadMore && noMore) LoadMoreStatus.NoMore(config.loadNoMoreMsg)
                else LoadMoreStatus.Gone
            var loadMoreOpt = opt
            if (it.forceRefresh) {
                loadMoreOpt = LoadMoreOpt.AllData(true)
            }
            val extra = VmExtra(status, loadMoreOpt)
            val res: VmPageResult<Key, ListValue, Value> = VmResult.Succeed(it, extra)
            res
        }.onStart {
            val msg = if (loadMore) config.loadMoreMsg else config.loadMsg
            emit(VmResult.Loading(msg, isLoadMore = loadMore))
        }.catch { t ->
            handleException(this, t, loadMore)
        }.flowOn(Dispatchers.IO)
    }

    open suspend fun handleException(
        flowCol: FlowCollector<VmPageResult<Key, ListValue, Value>>,
        t: Throwable,
        loadMore: Boolean
    ) {
        flowCol.emit(VmResult.Error(t, loadMore))
    }
}