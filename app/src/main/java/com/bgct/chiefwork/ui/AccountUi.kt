package com.bgct.chiefwork.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bgct.chiefwork.R
import com.bgct.chiefwork.bean.Account
import com.bgct.chiefwork.bean.AccountBean
import com.bgct.pagehelper.CreateLoadStatusUi
import com.bgct.pagehelper.LoadErrorDefault
import com.bgct.pagehelper.PageList
import com.bgct.pagehelper.RefreshView
import com.bgct.pagehelper.convertLoadStatus
import com.bgct.pagehelper.isShowDefault
import com.bgct.pagehelper.rememberLoadStatusHolder

/**
 * @copyright：hyc
 * @fileName: AccountUi.kt
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: Account list compose ui
 * @history:
 */

@Composable
fun AccountList(
    pageList: PageList<Account, AccountBean>,
    onRefresh: (() -> Unit) = {},
    onLoadMore: (() -> Unit) = {}
) {
    val loadMoreHolder = rememberLoadStatusHolder(onReachBottom = onLoadMore)
    //Obtain the load status to be displayed in the list based on VmResult
    val loadStatus = pageList.result.convertLoadStatus()
    val listState = rememberLazyListState()
    RefreshView(
        modifier = Modifier.nestedScroll(loadMoreHolder.nestedScrollConnection),
        onRefresh = onRefresh,
    ) {
        LazyColumn(state = listState, modifier = Modifier.background(Color.White)) {

            val dataList = pageList.datas
            if (dataList.isNotEmpty()) {
                itemsIndexed(dataList, key = { index, item ->
                    //We can't use item.id as the key here because we don't have a paging api
                    // and can only pull duplicate data to show the paging effect
                    //item.id
                    index
                }, contentType = { index, item ->
                    item.id
                }) { index, item ->
                    //正常数据项目
                    AccountItem(scope = this, account = item)
                    //分割线
                    if (index != dataList.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(16.dp, 0.dp),
                            color = Color.LightGray,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
            if (loadStatus.isShowDefault()) {
                item {
                    CreateLoadStatusUi(loadStatus = loadStatus,
                        onRefresh = onRefresh,
                        onLoadMore = onLoadMore,
                        loadError = {
                            LoadErrorDefault(
                                modifier = Modifier.fillParentMaxSize(),
                                onRefresh = onRefresh
                            )
                        })
                }
            }
        }
    }
}


@Composable
fun AccountItem(scope: LazyItemScope, account: Account) {

    Column(modifier = Modifier.padding(16.dp, 8.dp)) {
        Text(
            text = account.description,
            fontSize = 16.sp,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        AccountItemMoney(account = account)

        Spacer(modifier = Modifier.height(4.dp))


        AccountItemDate(account = account)

    }
}

@Composable
fun AccountItemDate(account: Account) {
    Row {
        Text(
            text = stringResource(id = R.string.category_str, account.category),
            fontSize = 12.sp,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis

        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = account.transactionDate,
            fontSize = 12.sp,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun AccountItemMoney(account: Account) {
    val debit = (account.debit ?: 0).toString()
    val credit = (account.credit ?: 0).toString()
    Row {

        Text(
            text = stringResource(id = R.string.debit_str, debit),
            fontSize = 12.sp,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.credit_str, credit),
            fontSize = 12.sp,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
        )
    }
}