package com.bgct.chiefwork.api

import com.bgct.chiefwork.bean.Account
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @copyright：hyc
 * @fileName: ChiefApi
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: chief api interface
 * @history:
 */
interface ChiefApi {
    //The backend interface does not provide a specific parameter name to control the page size
    // and which page is currently queried. The name is written here for the time being.
    // If you need to verify the real page logic, you can change the two names in the code
    @Headers("x-api-key:PMAK-668cefa85d9ee800012eef9d-7d7956c21099fa61f71001096a29b28fe7")
    @GET("bank/transaction")
    suspend fun getAccountData(
        @Query("page-index") index: Long,
        @Query("page-count") pageSize: Long
    ): List<Account>

    @Headers("x-api-key:PMAK-668cefa85d9ee800012eef9d-7d7956c21099fa61f71001096a29b28fe7")
    @GET()
    suspend fun getStr(): String
}