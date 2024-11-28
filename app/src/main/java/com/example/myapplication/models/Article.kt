package com.example.myapplication.models

import com.example.myapplication.misc.DateOnlySerialization
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
class Article(
    val index: Long,
    val title: String,
    @Serializable(DateOnlySerialization::class)
    val date: Date,
    val description: String,
)