package com.example.my_news_app.utils

sealed class SearchEvent {
    data class onProcessQuery(
        val isSearchProgressVisible: Boolean = true,
        val isCancelIconVisible: Boolean = false
    ) : SearchEvent()

    data class onQueryResult(
        val isSearchProgressVisible: Boolean = false,
        val isCancelIconVisible: Boolean = true
    ) : SearchEvent()
    data class idle(
        val isSearchProgressVisible: Boolean = false,
        val isCancelIconVisible: Boolean = false
    ) : SearchEvent()
}
