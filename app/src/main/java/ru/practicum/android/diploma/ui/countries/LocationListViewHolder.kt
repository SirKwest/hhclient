package ru.practicum.android.diploma.ui.countries

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LocationListItemBinding
import ru.practicum.android.diploma.domain.models.Location

class LocationListViewHolder<T : Location>(
    private val binding: LocationListItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(location: T) {
        binding.root.text = location.name
    }
}
