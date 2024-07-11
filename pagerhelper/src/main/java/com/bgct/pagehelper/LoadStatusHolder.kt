package com.bgct.pagehelper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ILoadStatusHolder {

    /**
     * 滚动协调器
     */
    val nestedScrollConnection: NestedScrollConnection
}

@Composable
fun rememberLoadStatusHolder(
    enabled: () -> Boolean = { true },
    onReachBottom: suspend () -> Unit
): ILoadStatusHolder {
    val coroutineScope = rememberCoroutineScope()

    return remember {
        LoadStatusHolder(enabled, onReachBottom, coroutineScope)
    }
}

private class LoadStatusHolder(
    enabled: () -> Boolean,
    onReachBottom: suspend () -> Unit,
    coroutineScope: CoroutineScope
) : ILoadStatusHolder {
    override val nestedScrollConnection = object : NestedScrollConnection {
        override suspend fun onPostFling(
            consumed: Velocity,
            available: Velocity
        ): Velocity {
            if (available.y < 0 && enabled()) {
                coroutineScope.launch {
                    onReachBottom()
                }
                return available
            } else {
                return Velocity.Zero
            }
        }
    }
}