package com.bgct.chiefwork.bean

/**
 * @copyright：hyc
 * @fileName: Account
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: main data source
 * @history:
 */
data class Account(
    val transactionDate: String,
    val description: String,
    val category: String,
    val debit: Double?,
    val credit: Double?,
    val id: Int,
)