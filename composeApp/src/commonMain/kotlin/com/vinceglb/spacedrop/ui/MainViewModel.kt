package com.vinceglb.spacedrop.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope

class MainScreenModel : ScreenModel {
    private val mainViewModel = SharedMainViewModel(screenModelScope)

    val state = mainViewModel.state
}