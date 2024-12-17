package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyListItemLayoutBinding
import ru.practicum.android.diploma.domain.models.VacancyShort

class VacancyListAdapter(private val vacancies: MutableList<VacancyShort>) : RecyclerView.Adapter<VacancyListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyListViewHolder {
        return VacancyListViewHolder(
            VacancyListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(holder: VacancyListViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(position) }
    }

    fun getItemByPosition(position: Int): VacancyShort {
        return vacancies[position]
    }

    fun resetData(data: List<VacancyShort>) {
        vacancies.clear()
        vacancies.addAll(data)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    private var itemClickListener: OnItemClickListener? = null
}
