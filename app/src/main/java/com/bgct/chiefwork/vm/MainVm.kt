package com.bgct.chiefwork.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgct.chiefwork.fetch.AccountPageSrc
import com.bgct.pagehelper.PageConfig
import com.bgct.pagehelper.PageFetch
import com.bgct.pagehelper.PageManager


/**
 * @copyright：hyc
 * @fileName: MainVm
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: Main viewmodel
 * @history:
 */
class MainVm : ViewModel() {
    //该分页加载管理器，用来管理分页加载逻辑
    val accountManager = PageManager(PageConfig(5)) {
        PageFetch(viewModelScope, it, AccountPageSrc(), 1)
    }

}