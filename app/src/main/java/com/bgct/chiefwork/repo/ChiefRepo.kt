package com.bgct.chiefwork.repo

import com.bgct.chiefwork.api.ChiefApi
import com.bgct.chiefwork.api.RetrofitManager
import com.bgct.chiefwork.bean.AccountBean

/**
 * @copyright：hyc
 * @fileName: ChiefRepo
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: Data warehouse, where all data operations take place
 * @history:
 */
object ChiefRepo {

    @JvmStatic
    suspend fun getAccountData(index: Long, pageSize: Long): AccountBean {
        val list =
            RetrofitManager.sInstance.create(ChiefApi::class.java).getAccountData(index, pageSize)
        val bean = AccountBean(list)
        return bean
    }
}