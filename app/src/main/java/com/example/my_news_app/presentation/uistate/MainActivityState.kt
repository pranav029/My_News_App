package com.example.my_news_app.presentation.uistate

data class MainActivityState(
    val isProgressDialogVisible: Boolean = true,
    val isAppbarVisible: Boolean = true,
    val searchIconClicked: Boolean = false
)
