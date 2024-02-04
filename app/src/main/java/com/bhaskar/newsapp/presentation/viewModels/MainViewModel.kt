package com.bhaskar.newsapp.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhaskar.newsapp.utils.NetworkResult
import com.bhaskar.newsapp.data.model.Article
import com.bhaskar.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: NewsRepository) : ViewModel(){

    val newsList = MutableLiveData<NetworkResult<List<Article>>>()

    fun getProduct() = viewModelScope.launch {
        newsList.postValue(repository.getNewsData())
    }

}