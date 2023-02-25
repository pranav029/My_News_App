package com.example.my_news_app.presentation.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseRecyclerViewAdapter<T, out A : DiffUtil.ItemCallback<T>> :
    RecyclerView.Adapter<ViewHolder>() {
    private var recyclerView: RecyclerView? = null
    private var mAsyncList: AsyncListDiffer<T>? = null
    protected val mList: List<T>
        get() = mAsyncList?.currentList ?: emptyList()


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindViewHolder(holder,mList[position],position)
    }

    fun submitList(newList: List<T>) {
        if (mAsyncList == null) mAsyncList = AsyncListDiffer(this, getDiffer())
        val recyclerViewCurrentState = recyclerView?.layoutManager?.onSaveInstanceState()
        mAsyncList?.submitList(newList)
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewCurrentState)
    } //?: throw Throwable("CoroutineScope must be provided to the RecyclerView Adapter")


    final override fun getItemCount(): Int = mAsyncList?.currentList?.size ?: 0

    protected abstract fun getDiffer(): A
    protected abstract fun onBindViewHolder(holder: ViewHolder,item:T,position: Int)
}