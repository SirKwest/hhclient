package ru.practicum.android.diploma.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LocationListItemBinding
import ru.practicum.android.diploma.domain.models.Location

class LocationListAdapter<T : Location> : RecyclerView.Adapter<LocationListViewHolder<T>>() {
    var locations: List<T> = emptyList()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = field.size

                override fun getNewListSize(): Int = value.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    field[oldItemPosition].id == value[newItemPosition].id

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean = field[oldItemPosition] == value[newItemPosition]
            })
            field = value
            diffResult.dispatchUpdatesTo(this)
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
