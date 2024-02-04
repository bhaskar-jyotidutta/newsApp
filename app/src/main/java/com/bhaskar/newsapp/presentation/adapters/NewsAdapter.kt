package com.bhaskar.newsapp.presentation.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhaskar.newsapp.R
import com.bhaskar.newsapp.data.model.Article
import com.bhaskar.newsapp.databinding.SingleNewsItemLayoutBinding
import com.bhaskar.newsapp.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class NewsAdapter(private val context: Context, private val newsList : List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val singleNewsItemLayoutBinding = SingleNewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsHolder(singleNewsItemLayoutBinding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val model = newsList[position]
        holder.binding.singleNewsTitleView.text = model.title
        holder.binding.singleNewsUrlView.text = model.url
        holder.binding.shimmerViewContainer.startShimmer()
        holder.binding.singleNewsDateView.text = Utils.convertTo12HourFormat(model.publishedAt)
        holder.binding.singleNewsTitleView.setOnClickListener {
            openBrowser(model.url)
        }
        Glide.with(holder.binding.singleNewsImageView.context)
            .load(model.urlToImage)
            .placeholder(R.drawable.ic_gallary)
            .listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                holder.binding.shimmerViewContainer.hideShimmer()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                holder.binding.shimmerViewContainer.hideShimmer()
                return false
            }
        }).into(holder.binding.singleNewsImageView)
    }

    class NewsHolder(val binding: SingleNewsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Method to open browser when news heading is clicked
     */
    private fun openBrowser(url: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}