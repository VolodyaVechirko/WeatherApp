package com.vvechirko.weatherapp.ui.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vvechirko.weatherapp.util.computeDiff
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private var computeJob: Job? = null
    private val diffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) = compareItems(oldItem, newItem)
        override fun areContentsTheSame(oldItem: T, newItem: T) = compareContents(oldItem, newItem)
    }

    abstract fun compareItems(oldItem: T, newItem: T): Boolean
    abstract fun compareContents(oldItem: T, newItem: T): Boolean

    var items: List<T> = listOf()
        set(value) {
            computeJob?.cancel()
            computeJob = launch {
                val result = withContext(Dispatchers.Default) {
                    computeDiff(field, value, diffCallback)
                }
                field = value
                result.dispatchUpdatesTo(this@BaseAdapter)
            }
        }

    override fun getItemCount(): Int = items.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        coroutineContext.cancelChildren()
    }
}