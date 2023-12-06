package com.vinceglb.spacedrop.ui

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.StateFlow

class MainViewModel() : KMMViewModel() {
    private val mainViewModel = SharedMainViewModel(viewModelScope.coroutineScope)

    val state: StateFlow<String> = mainViewModel.state
}
