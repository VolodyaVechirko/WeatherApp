package com.vvechirko.weatherapp.util

import androidx.recyclerview.widget.DiffUtil

fun <T> computeDiff(
    oldList: List<T>,
    newList: List<T>,
    diffCallback: DiffUtil.ItemCallback<T>
): DiffUtil.DiffResult {
    return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return diffCallback.getChangePayload(oldItem, newItem)
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem === newItem) {
                return true
            }
            return diffCallback.areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem === newItem) {
                return true
            }
            return diffCallback.areContentsTheSame(oldItem, newItem)

        }
    }, true)
}