package com.bgct.pagehelper

abstract class PageSource<Key, Input, ListValue, Value : ILoadList<ListValue>> {

    data class PageParam<Key, Input>(val loadSize: Int, val key: Key, val input: Input? = null)

    data class PageResult<Key, ListValue, Value : ILoadList<ListValue>>(
        val data: Value,
        val prevKey: Key? = null,
        val nextKey: Key? = null,
        val forceRefresh: Boolean = false
    )


    abstract suspend fun load(param: PageParam<Key, Input>): PageResult<Key, ListValue, Value>
}

abstract class PageSourceUnit<Key, ListValue, Value : ILoadList<ListValue>> :
    PageSource<Key, Unit, ListValue, Value>()

interface ILoadList<T> {
    fun getLoadList(): List<T>
}


typealias VmPageResult<Key, ListValue, Value> = VmResult<PageSource.PageResult<Key, ListValue, Value>>