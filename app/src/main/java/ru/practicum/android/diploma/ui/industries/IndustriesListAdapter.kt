package ru.practicum.android.diploma.ui.industries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustriesListItemBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesListAdapter : RecyclerView.Adapter<IndustriesListViewHolder>() {
    var industries: List<Industry> = emptyList()
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

    var onItemClickListener: OnItemClickListener? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesListViewHolder {
        return IndustriesListViewHolder(
            IndustriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: IndustriesListViewHolder, position: Int) {
        holder.bind(industries[position], position == selectedPosition)
        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClickListener?.onItemClick(industries[position].id)
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    fun interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}
