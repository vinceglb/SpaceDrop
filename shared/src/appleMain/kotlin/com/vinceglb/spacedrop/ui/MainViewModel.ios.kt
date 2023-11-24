package com.vinceglb.spacedrop.ui

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope

class MainViewModel() : KMMViewModel() {
    private val mainViewModel = SharedMainViewModel(viewModelScope.coroutineScope)

    val state = mainViewModel.state
}
