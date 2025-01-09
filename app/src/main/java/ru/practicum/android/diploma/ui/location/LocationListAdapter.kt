package ru.practicum.android.diploma.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LocationListItemBinding
import ru.practicum.android.diploma.domain.models.Location

class LocationListAdapter<T : Location> : RecyclerView.Adapter<LocationListViewHolder<T>>() {
    var locations: List<T> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationListViewHolder<T> {
        return LocationListViewHolder(
            LocationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationListViewHolder<T>, position: Int) {
        holder.bind(locations[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(locations[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    fun interface OnItemClickListener<T> {
        fun onItemClick(location: T)
    }
}
