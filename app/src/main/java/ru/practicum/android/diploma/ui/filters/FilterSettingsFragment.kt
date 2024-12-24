package ru.practicum.android.diploma.ui.filters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.presentation.FilterSettingsViewModel

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

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            setPreviousFilters(state.filterSettings)
        }
        viewModel.observeSalaryValueState().observe(viewLifecycleOwner) {state ->
            setSalaryFieldIconAndListener(state.isNullOrBlank())
        }
        viewModel.observeApplyButtonState().observe(viewLifecycleOwner) { state ->
            binding.applyBtn.isVisible = state
        }
        viewModel.observeResetButtonState().observe(viewLifecycleOwner) { state ->
            binding.resetBtn.isVisible = state
        }

        binding.onlyWithSalaryTv.setOnClickListener {
            binding.onlyWithSalaryTv.toggle()
            viewModel.updateOnlyWithSalaryValue(binding.onlyWithSalaryTv.isChecked)
        }

        binding.applyBtn.setOnClickListener {
            val salary =
                if (binding.salaryEt.text.toString().isBlank()) {
                    null
                } else {
                    binding.salaryEt.text.toString().toInt()
                }

            val filter = Filter(
                binding.locationEt.text.toString(),
                binding.industryEt.text.toString(),
                salary,
                binding.onlyWithSalaryTv.isChecked
            )
            viewModel.saveFilter(filter)
            findNavController().navigateUp()
        }

        settingListeners()

        binding.industryEt.setOnClickListener {
            findNavController().navigate(R.id.industries_fragment)
        }
        binding.locationEt.setOnClickListener {
            findNavController().navigate(R.id.work_location_fragment)
        }
        viewModel.getFilter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingListeners() {
        binding.salaryEt.doOnTextChanged { text, _, _, _ ->
            viewModel.updateSalaryValue(text.toString())
        }
        binding.resetBtn.setOnClickListener {
            viewModel.resetFilters()
        }
    }

    private fun setPreviousFilters(filter: Filter?) {
        if (filter == null) {
            return
        }
        binding.resetBtn.isVisible = true
        binding.onlyWithSalaryTv.isChecked = filter.isExistSalary

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
            binding.salaryTil.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete)
            binding.salaryTil.setEndIconOnClickListener {
                viewModel.updateSalaryValue("")
                binding.salaryEt.setText("")
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.salaryEt.clearFocus()
            }
        }
    }

    private fun setLocationEmptyValue() {
        binding.locationEt.setText("")
        binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_arrow_forward
        )
        binding.locationTil.setOnClickListener {
            findNavController().navigate(R.id.action_filter_settings_fragment_to_work_location_fragment)
        }
    }

    private fun setLocationValue(value: String) {
        binding.locationEt.setText(value)
        binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_delete
        )
        binding.locationTil.setOnClickListener {
            setLocationEmptyValue()
        }
    }

    private fun setIndustryEmptyValue() {
        binding.industryEt.setText("")
        binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_arrow_forward
        )
        binding.industryTil.setOnClickListener {
            findNavController().navigate(R.id.action_filter_settings_fragment_to_industries_fragment)
        }
    }

    private fun setIndustryValue(value: String) {
        binding.industryEt.setText(value)
        binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_delete
        )
        binding.industryTil.setOnClickListener {
            setLocationEmptyValue()
        }
    }
}
