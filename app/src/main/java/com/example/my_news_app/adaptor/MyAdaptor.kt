package com.example.my_news_app.adaptor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.my_news_app.FullNews
import com.example.my_news_app.R
import com.example.my_news_app.data
import com.example.my_news_app.storage.Article

class MyAdaptor(private val mList: List<Article>,private val con:Context) : RecyclerView.Adapter<MyAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        // holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        Glide.with(con).load(ItemsViewModel.urlToImage).into(holder.imageView)
        holder.heading.text = ItemsViewModel.title
        holder.description.text=ItemsViewModel.description
        holder.itemView.setOnClickListener(){
            var intent=Intent(con,FullNews::class.java)
            intent.putExtra("URL",ItemsViewModel.url.toString())
            con.startActivity(intent)

        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val heading: TextView = itemView.findViewById(R.id.heading)
        val description: TextView=itemView.findViewById(R.id.description)
    }
}