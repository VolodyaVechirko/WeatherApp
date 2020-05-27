package com.vvechirko.weatherapp.ui.details

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.core.util.toCelsius
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseAdapter
import com.vvechirko.weatherapp.util.inflate
import kotlinx.android.synthetic.main.item_forecast.view.*
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(
    private val itemClickListener: ItemClickListener
) : BaseAdapter<ForecastEntity, ForecastAdapter.Holder>() {

    interface ItemClickListener {
        fun onItemClicked(item: ForecastEntity)
    }

    override fun compareItems(oldItem: ForecastEntity, newItem: ForecastEntity): Boolean {
        return oldItem.date == newItem.date
    }

    override fun compareContents(oldItem: ForecastEntity, newItem: ForecastEntity): Boolean {
        return oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.item_forecast))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClicked(item)
        }
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val dateFormat = SimpleDateFormat("EEEE\nHH:mm", Locale.US)
        fun bind(it: ForecastEntity) = with(itemView) {
            icon.load(it.icon)
            degree.text = it.temp.toCelsius()
            date.text = dateFormat.format(it.date)
        }
    }
}