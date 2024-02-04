package com.bhaskar.newsapp.presentation.activities

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskar.newsapp.R
import com.bhaskar.newsapp.data.model.Article
import com.bhaskar.newsapp.databinding.ActivityMainBinding
import com.bhaskar.newsapp.databinding.PopupLayoutBinding
import com.bhaskar.newsapp.domain.repository.NewsRepository
import com.bhaskar.newsapp.presentation.adapters.NewsAdapter
import com.bhaskar.newsapp.presentation.viewModels.MainViewModel
import com.bhaskar.newsapp.presentation.viewModels.MainViewModelFactory
import com.bhaskar.newsapp.utils.NetworkResult
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var repository: NewsRepository
    private lateinit var mainViewModel: MainViewModel
    private lateinit var factory: MainViewModelFactory
    private lateinit var adapter: NewsAdapter
    private lateinit var articleList : List<Article>
    private var newestNewsFirstView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        init()
        setToolbar()
        setRecyclerview()
        fetchNews()
        setObserver()
        requestPermission()
    }

    /**
     * Initialize view model
     */
    private fun init(){
        repository = NewsRepository()
        factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    /**
     * Set toolbar
     */
    private fun setToolbar() {
        setSupportActionBar(mainBinding.mainToolbar)
    }


    /**
     * Set recyclerview adapter
     */
    private fun setRecyclerview() {
        mainBinding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * To fetch news articles from API
     */
    private fun fetchNews() {
        mainBinding.mainProgressBar.visibility = View.VISIBLE
        mainViewModel.getProduct()
    }

    /**
     * Set observers for news
     */
    private fun setObserver() {
        mainViewModel.newsList.observe(this){
            when(it){
                is NetworkResult.Success -> {
                    mainBinding.mainProgressBar.visibility = View.GONE
                    articleList = it.data!!
                    adapter = NewsAdapter(this@MainActivity, articleList)
                    mainBinding.mainRecyclerView.adapter = adapter
                }
                is NetworkResult.Error -> {
                    mainBinding.mainErrorView.visibility = View.VISIBLE
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.main_menu_short){
            showShortDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Dialog to show shorting
     */
    private fun showShortDialog() {
        val popupLayoutBinding = PopupLayoutBinding.inflate(layoutInflater)
        if(newestNewsFirstView){
            popupLayoutBinding.newestNewsFirstView.visibility = View.GONE
            popupLayoutBinding.oldestNewsFirstView.visibility = View.VISIBLE
        }else{
            popupLayoutBinding.newestNewsFirstView.visibility = View.VISIBLE
            popupLayoutBinding.oldestNewsFirstView.visibility = View.GONE
        }
        val builder = AlertDialog.Builder(this)
        builder.setView(popupLayoutBinding.root)
        val alertDialog = builder.create()
        alertDialog.show()
        popupLayoutBinding.newestNewsFirstView.setOnClickListener {
            articleList = sortArticlesByDate(articleList, false)
            mainBinding.mainRecyclerView.adapter = NewsAdapter(this@MainActivity, articleList)
            alertDialog.dismiss()
            newestNewsFirstView = true
        }
        popupLayoutBinding.oldestNewsFirstView.setOnClickListener {
            articleList = sortArticlesByDate(articleList, true)
            mainBinding.mainRecyclerView.adapter = NewsAdapter(this@MainActivity, articleList)
            alertDialog.dismiss()
            newestNewsFirstView = false
        }
    }

    /**
     * To order the news list
     */
    private fun sortArticlesByDate(articles: List<Article>, ascending: Boolean): List<Article> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return if (ascending) {
            articles.sortedBy {
                dateFormatter.parse(it.publishedAt)
            }
        } else {
            articles.sortedByDescending {
                dateFormatter.parse(it.publishedAt)
            }
        }
    }

    private fun hasLocationPermission() : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)== PackageManager.PERMISSION_GRANTED
        }else{
            return true
        }
    }

    /**
     * Method to check is permission allowed and ask for it
     */
    private fun requestPermission(){
        val permissionRequestList = mutableListOf<String>()
        if(!hasLocationPermission()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionRequestList.add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if(permissionRequestList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionRequestList.toTypedArray(), 0)
        }

    }

}