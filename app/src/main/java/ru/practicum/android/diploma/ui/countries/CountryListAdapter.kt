package ru.practicum.android.diploma.ui.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountriesListItemBinding
import ru.practicum.android.diploma.domain.models.Country

class CountryListAdapter(private val countries: List<Country>) : RecyclerView.Adapter<CountryListViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListViewHolder {
        return CountryListViewHolder(
            CountriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CountryListViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(countries[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun interface OnItemClickListener {
        fun onItemClick(id: Country)
    }
}
