package com.bgct.pagehelper

data class PageConfig(
    //每一页的大小
    val pageSize: Int,
    //加载时显示的提示信息
    val loadMsg: String = "",
    //加载更多时显示的提示信息
    val loadMoreMsg: String = "",
    //没有更多时的提示信息
    val loadNoMoreMsg: String = "",
    //数据为空或者错误时的提示信息
    val emptyMsg: String = "",
)