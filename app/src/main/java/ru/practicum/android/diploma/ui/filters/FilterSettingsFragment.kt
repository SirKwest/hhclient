package ru.practicum.android.diploma.ui.filters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.displayName
import ru.practicum.android.diploma.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.ui.search.SearchFragment.Companion.FILTERS_DATA_KEY
import ru.practicum.android.diploma.ui.search.SearchFragment.Companion.FILTERS_RESULT_KEY

class FilterSettingsFragment : Fragment() {
    private var _binding: FragmentFilterSettingsBinding? = null
    private val viewModel: FilterSettingsViewModel by viewModel()
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            setScreenState(state.filterSettings)
        }
        viewModel.observeSalaryValueState().observe(viewLifecycleOwner) { state ->
            setSalaryFieldIconAndListener(state.isNullOrBlank())
        }

        settingListeners()

        viewModel.initializeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingListeners() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.salaryEt.doOnTextChanged { text, _, _, _ ->
            viewModel.updateSalaryValue(text.toString())
        }
        binding.resetBtn.setOnClickListener {
            viewModel.resetFilters()
            setFragmentResult(FILTERS_RESULT_KEY, bundleOf(FILTERS_DATA_KEY to false))
            findNavController().navigateUp()
        }
        binding.onlyWithSalaryTv.setOnClickListener {
            binding.onlyWithSalaryTv.toggle()
            viewModel.updateOnlyWithSalaryValue(binding.onlyWithSalaryTv.isChecked)
        }

        binding.applyBtn.setOnClickListener {
            viewModel.saveFilter()
            setFragmentResult(FILTERS_RESULT_KEY, bundleOf(FILTERS_DATA_KEY to true))
            findNavController().navigateUp()
        }
    }

    private fun setScreenState(filter: Filter) {
        if (filter != Filter()) {
            binding.applyBtn.visibility = View.VISIBLE
            binding.resetBtn.visibility = View.VISIBLE
        } else {
            binding.applyBtn.visibility = View.GONE
            binding.resetBtn.visibility = View.GONE
        }
        binding.onlyWithSalaryTv.isChecked = filter.isExistSalary == true

        if (filter.salary == null) {
            binding.salaryEt.setText("")
        } else {
            binding.salaryEt.setText(filter.salary.toString())
        }
        if (filter.workPlace == null) {
            setLocationEmptyValue()
        } else {
            setLocationValue(filter.workPlace)
        }
        if (filter.industry == null) {
            setIndustryEmptyValue()
        } else {
            setIndustryValue(filter.industry)
        }
    }

    private fun setSalaryFieldIconAndListener(isEmpty: Boolean) {
        if (isEmpty) {
            binding.salaryTil.endIconDrawable = null
            binding.salaryTil.setEndIconOnClickListener(null)
        } else {
            binding.applyBtn.visibility = View.VISIBLE
            binding.resetBtn.visibility = View.VISIBLE
            binding.salaryTil.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete)
            binding.salaryTil.setEndIconOnClickListener {
                viewModel.clearSalaryValue()
                binding.salaryEt.setText("")
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.salaryEt.clearFocus()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setLocationEmptyValue() {
        binding.locationEt.setText("")
        binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_arrow_forward
        )
        binding.locationEt.setOnClickListener {
            findNavController().navigate(R.id.action_filter_settings_fragment_to_work_location_fragment)
        }
        binding.locationEt.setOnTouchListener(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setLocationValue(value: Area) {
        binding.locationEt.setText(value.displayName())
        binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_delete
        )
        binding.locationEt.setOnClickListener {
            findNavController().navigate(R.id.action_filter_settings_fragment_to_work_location_fragment)
        }

        binding.locationEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.locationEt.compoundDrawablesRelative[2]
                val position = binding.locationEt.width -
                    binding.locationEt.paddingEnd - drawableEnd.bounds.width()
                if (drawableEnd != null && event.rawX >= position) {
                    viewModel.resetArea()
                    setLocationEmptyValue()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setIndustryEmptyValue() {
        binding.industryEt.setText("")
        binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_arrow_forward
        )
        binding.industryEt.setOnClickListener {
            findNavController().navigate(R.id.action_filter_settings_fragment_to_industries_fragment)
        }
        binding.industryEt.setOnTouchListener(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setIndustryValue(value: Industry) {
        binding.industryEt.setText(value.name)
        binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_delete
        )
        binding.industryEt.setOnClickListener {
            findNavController().navigate(R.id.action_filter_settings_fragment_to_industries_fragment)
        }

        binding.industryEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.industryEt.compoundDrawablesRelative[2]
                val position = binding.industryEt.width -
                    binding.industryEt.paddingEnd - drawableEnd.bounds.width()
                if (drawableEnd != null && event.rawX >= position) {
                    viewModel.resetIndustry()
                    setIndustryEmptyValue()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }
}
