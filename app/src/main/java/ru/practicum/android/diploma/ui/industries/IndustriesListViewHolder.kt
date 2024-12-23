package ru.practicum.android.diploma.ui.industries

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustriesListItemBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesListViewHolder(private val binding: IndustriesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(industry: Industry, isChecked: Boolean) {
        binding.root.text = industry.name
        binding.root.isChecked = isChecked
    }
}
