package com.bhaskar.newsapp.domain.repository

import com.bhaskar.newsapp.utils.NetworkResult
import com.bhaskar.newsapp.data.model.Article
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class NewsRepository() {

    /**
     * Fetch data from API
     */
    suspend fun getNewsData() : NetworkResult<List<Article>> {
        return withContext(Dispatchers.IO){
            val url = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream = httpURLConnection.inputStream
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            httpURLConnection.disconnect()
            if(jsonObject.optString("status") == "ok"){
                NetworkResult.Success(parseJson(jsonObject))
            }else{
                NetworkResult.Error("An error occurred")
            }
        }
    }

    /*
    * convert jsonString to Kotlin Article object and return as list
    * */
    private fun parseJson(jsonObject: JSONObject): List<Article>{
        val articlesArray = jsonObject.getJSONArray("articles")
        val articleList = mutableListOf<Article>()
        val gson = Gson()
        for(i in 0 until articlesArray.length()){
            val singleArticleObject = articlesArray.getJSONObject(i)
            val singleArticle = gson.fromJson(singleArticleObject.toString(), Article::class.java)
            articleList.add(singleArticle)
        }
        return articleList
    }

}