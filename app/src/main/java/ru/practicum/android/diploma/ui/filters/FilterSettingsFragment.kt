package ru.practicum.android.diploma.ui.filters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding

class FilterSettingsFragment : Fragment() {
    private var _binding: FragmentFilterSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun changeExpectedSalaryTextViewState(isSalaryEntered: Boolean, isSalaryEnteringFinished: Boolean) {
        if (isSalaryEntered) {
            binding.filterExpectedSalaryTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            if (isSalaryEnteringFinished) {
                binding.filterExpectedSalaryTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
        } else {
            binding.filterExpectedSalaryTextView.setTextAppearance(R.style.FilterSmallerTextStyle)
        }
    }

    fun changeFilterTextViewState(isFilterChosen: Boolean, view: TextView) {
        if (isFilterChosen) {
            view.setTextAppearance(R.style.FilterFragmentActiveTextStyle)
        } else {
            view.setTextAppearance(R.style.FilterFragmentInactiveTextStyle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
