package com.bgct.chiefwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bgct.chiefwork.ui.AccountList
import com.bgct.chiefwork.ui.theme.ChiefWorkTheme
import com.bgct.chiefwork.vm.MainVm
import com.bgct.pagehelper.PageEvent
import com.bgct.pagehelper.PageList

/**
 * @copyright：hyc
 * @fileName: MainActivity
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: main ui
 * @history:
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChiefWorkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val vm: MainVm = viewModel()
    val bean by vm.accountManager.flow.collectAsState(PageList())

    LaunchedEffect("Refresh") {
        vm.accountManager.sendEvent(PageEvent.Refresh)
    }

    AccountList(bean, onRefresh = {
        vm.accountManager.sendEvent(PageEvent.Refresh)
    }) {
        vm.accountManager.sendEvent(PageEvent.Next)
    }
}


