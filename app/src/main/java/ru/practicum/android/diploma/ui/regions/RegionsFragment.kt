package ru.practicum.android.diploma.ui.regions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.databinding.FragmentRegionsBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.RegionsViewModel
import ru.practicum.android.diploma.ui.location.WorkLocationFragment
import ru.practicum.android.diploma.util.getSerializableData

class RegionsFragment : Fragment() {
    private var _binding: FragmentRegionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegionsViewModel by viewModel {
        val selectedCountry = requireArguments().getSerializableData<Country>(WorkLocationFragment.REGION_DATA_KEY)
        val selectedCountryId = selectedCountry?.id ?: ""
        parametersOf(selectedCountryId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
