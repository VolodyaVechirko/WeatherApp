package com.vvechirko.weatherapp.ui.select

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.weatherapp.R
import kotlinx.android.synthetic.main.item_city.view.*

class CitiesAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<CitiesAdapter.Holder>() {

    interface ItemClickListener {
        fun onItemClicked(item: CityEntity)
    }

    var items: List<CurrentWeather> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_city, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClicked(item.city)
        }
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(it: CurrentWeather) = with(itemView) {
            icon.load(it.forecast.icon)
            name.text = it.city.name
            degree.text = it.forecast.tempMinMax
        }
    }
}