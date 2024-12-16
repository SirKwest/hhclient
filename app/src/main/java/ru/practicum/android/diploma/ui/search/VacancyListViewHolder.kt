package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListItemLayoutBinding
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.util.SalaryDescriptionBuilder

class VacancyListViewHolder(private val binding: VacancyListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(vacancy: VacancyShort) {
        Glide.with(itemView)
            .load(vacancy.logo)
            .placeholder(R.drawable.icon_placeholder_logo)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dim_8dp)))
            .into(binding.vacancyLogoIv)
        binding.vacancyNameTv.text = vacancy.name
        binding.vacancyEmployerTv.text = vacancy.employer
        binding.vacancySalaryTv.text = SalaryDescriptionBuilder(binding.root.context)
            .build(vacancy.salaryLow, vacancy.salaryHigh, vacancy.currency)
    }
}
