package com.example.my_news_app.data.remote.dto

import com.example.my_news_app.domain.model.Source as Source

data class SourceDto(
    val id: String?,
    val name: String?
)

fun SourceDto.toSource(): Source {
    return Source(
        id = id,
        name = name
    )
}