package com.bgct.pagehelper

class PageList<ListValue, T : ILoadList<ListValue>> {
    private val _datas = mutableListOf<ListValue>()

    val datas: List<ListValue>
        get() = _datas
    private var _result: VmResult<T> = VmResult.Loading()
    val result: VmResult<T>
        get() = _result


    fun copy(): PageList<ListValue, T> {
        val newRes = PageList<ListValue, T>()
        newRes._datas.addAll(_datas)
        newRes._result = _result
        return newRes
    }

    fun addResult(res: VmResult<T>) {
        _result = res
        if (res is VmResult.Succeed) {
            val opt = res.extra.opt
            val list = res.data.getLoadList()
            if (opt is LoadMoreOpt.Append) {
                _datas.addAll(list)
            } else if (opt is LoadMoreOpt.Prepend) {
                _datas.addAll(0, list)
            } else {
                _datas.clear()
                _datas.addAll(list)
            }
        } else if (res.isError()) {
            _datas.clear()
        } else if (res.isLoading()) {

        }
    }

}