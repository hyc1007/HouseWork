package com.bgct.chiefwork.fetch

import com.bgct.chiefwork.bean.Account
import com.bgct.chiefwork.bean.AccountBean
import com.bgct.chiefwork.repo.ChiefRepo
import com.bgct.pagehelper.PageSourceUnit

/**
 * @copyright：hyc
 * @fileName: AccountPageSrc
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: Used to control paging and loading logic
 * @history:
 */
class AccountPageSrc : PageSourceUnit<Long, Account, AccountBean>() {
    override suspend fun load(param: PageParam<Long, Unit>): PageResult<Long, Account, AccountBean> {
        val size = param.loadSize
        val index = param.key
        val data = ChiefRepo.getAccountData(index, size.toLong())
        //This should be based on the backend return result to determine whether to reach the end of the list,
        // this time the backend does not provide relevant fields, so the next page number directly to index+1
        val nextIndex = index + 1
        return PageResult(data, null, nextIndex)
    }
}