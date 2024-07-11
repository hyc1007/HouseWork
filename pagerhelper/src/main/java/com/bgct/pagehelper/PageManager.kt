package com.bgct.pagehelper

import kotlinx.coroutines.flow.Flow

open class PageManager<Key, Input, ListValue, Value : ILoadList<ListValue>>(
    private val config: PageConfig,
    pageFetchCreate: (PageConfig) -> PageFetch<Key, Input, ListValue, Value>,
) {
    private val pageFetch = pageFetchCreate.invoke(config)

    val flow: Flow<PageList<ListValue, Value>> = pageFetch.flow

    fun sendEvent(event: PageEvent) {
        pageFetch.sendEvent(event)
    }


}

sealed class PageEvent {
    //刷新页面
    object Refresh : PageEvent()

    //加载上一页数据
    object Next : PageEvent()

    //加载上一页数据
    object Prev : PageEvent()
}