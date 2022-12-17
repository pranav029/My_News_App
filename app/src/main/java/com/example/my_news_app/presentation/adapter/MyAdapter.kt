package com.example.my_news_app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_news_app.R
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.ClickCallBack

class MyAdapter(private val mList: List<Article>, private val mListener: ClickCallBack) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article = mList[position]

        // sets the image to the imageview from our itemHolder class
        // holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.apply {
            Glide.with(heading.context).load(article.urlToImage)
                .placeholder(R.drawable.mrvsmk2pl3l8fwocbfhy).into(imageView)
            article.title?.let { heading.text = it }
            description.text = article.description
            itemView.setOnClickListener() {
                mListener.onArticleClick(article.url.toString())
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int = mList.size

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val heading: TextView = itemView.findViewById(R.id.heading)
        val description: TextView = itemView.findViewById(R.id.description)
    }
}