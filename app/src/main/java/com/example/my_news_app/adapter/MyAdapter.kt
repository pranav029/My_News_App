package com.example.my_news_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_news_app.activities.FullNews
import com.example.my_news_app.R
import com.example.my_news_app.modal.Article

class MyAdapter(private val mList: List<Article>, private val con:Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
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
        holder.apply {
            Glide.with(con).load(ItemsViewModel.urlToImage).into(imageView)
            heading.text = ItemsViewModel.title
            description.text=ItemsViewModel.description
            itemView.setOnClickListener(){
                var intent=Intent(con, FullNews::class.java)
                intent.putExtra("URL",ItemsViewModel.url.toString())
                con.startActivity(intent)
            }
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