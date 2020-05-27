package com.vvechirko.weatherapp.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.core.util.toCelsius
import com.vvechirko.weatherapp.R
import kotlinx.android.synthetic.main.item_forecast.view.*
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<ForecastAdapter.Holder>() {

    interface ItemClickListener {
        fun onItemClicked(item: ForecastEntity)
    }

    var items: List<ForecastEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forecast, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

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