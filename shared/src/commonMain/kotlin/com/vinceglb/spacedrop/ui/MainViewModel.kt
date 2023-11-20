package com.vinceglb.spacedrop.ui

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

class SharedMainViewModel(
    coroutineScope: CoroutineScope
) {
    val state: MutableStateFlow<String> = MutableStateFlow("?")

    init {
        coroutineScope.launch {
            delay(1000)
            state.value = "plop"
        }
    }
}
