package ru.practicum.android.diploma.ui.countries

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountriesListItemBinding
import ru.practicum.android.diploma.domain.models.Country

class CountryListViewHolder(private val binding: CountriesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(country: Country) {
        binding.root.text = country.name
    }
}
