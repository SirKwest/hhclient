package ru.practicum.android.diploma.ui.filters

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding

class FilterSettingsFragment : Fragment() {
    private var _binding: FragmentFilterSettingsBinding? = null
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
        binding.onlyWithSalaryTv.setOnClickListener {
            binding.onlyWithSalaryTv.toggle()
        }
        binding.applyBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        settingListenersForTesting()
        settingListenersForReal()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingListenersForReal() {
        binding.salaryEt.doOnTextChanged { text, _, _, _ ->
            val drawableEnd: Drawable? = if (text?.isNotBlank() == true) {
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete)
            } else {
                null
            }
            binding.salaryTil.endIconDrawable = drawableEnd

            if (text?.isNotBlank() == true) {
                binding.salaryTil.setEndIconOnClickListener {
                    binding.salaryEt.setText("")
                    val inputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
                    binding.salaryEt.clearFocus()
                }
            }
        }

    }

    private fun settingListenersForTesting() {
        binding.locationEt.setOnClickListener {
            if (binding.locationEt.text.isNullOrBlank()) {
                binding.locationEt.setText("Москва")
                binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_delete)
                binding.resetBtn.isVisible = true
                binding.applyBtn.isVisible = true
            } else {
                binding.locationEt.setText("")
                binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_arrow_forward)
                binding.resetBtn.isVisible = false
                binding.applyBtn.isVisible = false
            }
        }

        binding.resetBtn.setOnClickListener {
            binding.locationEt.setText("")
            binding.locationTil.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_arrow_forward)
            binding.industryEt.setText("")
            binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_arrow_forward)
            binding.resetBtn.isVisible = false
            binding.applyBtn.isVisible = false
        }

        binding.industryEt.setOnClickListener {
            if (binding.industryEt.text.isNullOrBlank()) {
                binding.industryEt.setText("Администрирование")
                binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_delete)
            } else {
                binding.industryEt.setText("")
                binding.industryTil.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_arrow_forward)
            }
        }
    }

}
