package com.vvechirko.weatherapp.ui.select

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.util.inflate
import kotlinx.android.synthetic.main.item_city.view.*

const val HEADER_HOLDER = 1
const val ITEM_HOLDER = 2

class CitiesAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClicked(item: CityEntity)
    }

    var displayHeader: Boolean = false

    var items: List<CurrentWeather> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (displayHeader && position == 0) HEADER_HOLDER else ITEM_HOLDER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_HOLDER) Header(parent.inflate(R.layout.item_header))
        else Item(parent.inflate(R.layout.item_city))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (getItemViewType(position) == HEADER_HOLDER) {
            (holder as Header).bind(item)
        } else {
            (holder as Item).bind(item)
        }
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClicked(item.city)
        }
    }

    class Item(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(it: CurrentWeather) = with(itemView) {
            icon.load(it.forecast.icon)
            name.text = it.city.name
            degree.text = it.forecast.tempMinMax
        }
    }

    class Header(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(it: CurrentWeather) = with(itemView) {
            icon.load(it.forecast.icon)
            name.text = it.city.name
            degree.text = it.forecast.tempMinMax
        }
    }
}