package com.example.myapplication.repository.datasource

import android.content.Context
import com.example.myapplication.R
import com.example.myapplication.models.Article
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cloud @Inject constructor(
    @ApplicationContext val context: Context,
) {
    suspend fun listArticles(): List<Article> {
        // simulate loading from the server
        delay(1500)
        val json = context.resources.openRawResource(R.raw.sample_data_list).reader().use {
            it.readText()
        }
        return Json.decodeFromString(ListSerializer(Article.serializer()), json)
    }
}