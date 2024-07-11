package com.bgct.chiefwork.bean

import com.bgct.pagehelper.ILoadList

/**
 * @copyright：hyc
 * @fileName: AccountBean
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description:
 * @history:
 */
class AccountBean(val list: List<Account>) : ILoadList<Account> {
    override fun getLoadList(): List<Account> {
        return list
    }
}