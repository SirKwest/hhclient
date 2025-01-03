package ru.practicum.android.diploma.ui.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentWorkLocationBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.WorkLocationFragmentViewModel
import ru.practicum.android.diploma.util.getSerializableData

class WorkLocationFragment : Fragment() {
    private var _binding: FragmentWorkLocationBinding? = null
    private val viewModel: WorkLocationFragmentViewModel by viewModel()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeApplyButtonState().observe(viewLifecycleOwner) { state ->
            binding.selectButton.isVisible = state
        }
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            countryEt.setOnClickListener {
                findNavController().navigate(R.id.countries_fragment)
            }
            regionEt.setOnClickListener {
                findNavController().navigate(
                    R.id.regions_fragment,
                    bundleOf(REGION_DATA_KEY to viewModel.getCountryValue())
                )
            }

            selectButton.setOnClickListener {
                viewModel.saveAreaToFilter()
                findNavController().navigateUp()
            }

            setFragmentResultListener(COUNTRY_RESULT_KEY) { _, bundle ->
                bundle.getSerializableData<Country>(COUNTRY_DATA_KEY)?.let {
                    viewModel.setCountryValue(it)
                    setCountryValue(it)
                    if (viewModel.isSelectedRegionShouldBeRemoved()) {
                        setRegionEmptyValue()
                    }
                }
            }

            setFragmentResultListener(REGION_RESULT_KEY) { _, bundle ->
                bundle.getSerializableData<Region>(REGION_DATA_KEY)?.let {
                    viewModel.setRegionValue(it)
                    setRegionValue(it)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.apply {
            if (countryEt.text?.isNotBlank() == true) {
                setClearButton(binding.countryTil) { setCountryEmptyValue() }
            }

            if (regionEt.text?.isNotBlank() == true) {
                setClearButton(binding.regionTil) { setRegionEmptyValue() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRegionEmptyValue() {
        binding.regionEt.setText("")
        binding.regionTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_arrow_forward
        )
        binding.regionEt.setOnTouchListener(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setCountryEmptyValue() {
        binding.countryEt.setText("")
        binding.countryTil.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_arrow_forward
        )
        binding.countryEt.setOnTouchListener(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setCountryValue(value: Country) {
        binding.countryEt.setText(value.name)
        setClearButton(binding.countryTil) { setCountryEmptyValue() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRegionValue(value: Region) {
        binding.regionEt.setText(value.name)
        setClearButton(binding.regionTil) { setRegionEmptyValue() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClearButton(til: TextInputLayout, onClear: () -> Unit) {
        til.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.icon_delete
        )
        til.editText?.let {
            it.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableEnd = it.compoundDrawablesRelative[2]
                    val position = it.width - it.paddingEnd - drawableEnd.bounds.width()
                    if (drawableEnd != null && event.rawX >= position) {
                        onClear()
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

    companion object {
        const val COUNTRY_RESULT_KEY = "COUNTRY_RESULT_KEY"
        const val COUNTRY_DATA_KEY = "COUNTRY"
        const val REGION_RESULT_KEY = "REGION_RESULT_KEY"
        const val REGION_DATA_KEY = "REGION"
    }
}
