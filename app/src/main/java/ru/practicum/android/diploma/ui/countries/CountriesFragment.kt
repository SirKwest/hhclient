package ru.practicum.android.diploma.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountriesBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.CountriesFragmentState
import ru.practicum.android.diploma.presentation.CountriesViewModel
import ru.practicum.android.diploma.ui.location.LocationListAdapter
import ru.practicum.android.diploma.ui.location.WorkLocationFragment

class CountriesFragment : Fragment() {
    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.observeScreenState().observe(viewLifecycleOwner, ::processState)
    }

    private fun processState(state: CountriesFragmentState) {
        when (state) {
            is CountriesFragmentState.ShowingResults -> showResults(state.countries)
            is CountriesFragmentState.NoInternetAccess -> showNoInternet()
            is CountriesFragmentState.RequestInProgress -> showRequestInProgress()
            is CountriesFragmentState.ServerError -> showServerError()
        }
    }

    private fun showResults(countries: List<Country>) {
        binding.apply {
            countriesRecyclerView.isVisible = true
            countriesRecyclerView.adapter = LocationListAdapter<Country>().apply {
                locations = countries
                onItemClickListener = LocationListAdapter.OnItemClickListener { countryItem ->
                    setFragmentResult(
                        WorkLocationFragment.COUNTRY_RESULT_KEY,
                        bundleOf(WorkLocationFragment.COUNTRY_DATA_KEY to countryItem)
                    )
                    findNavController().navigateUp()
                }
            }

            noInternetStub.isVisible = false
            serverErrorStub.isVisible = false
            loading.isVisible = false

        }
    }

    private fun showNoInternet() {
        binding.apply {
            noInternetStub.isVisible = true

            countriesRecyclerView.isVisible = false
            serverErrorStub.isVisible = false
            loading.isVisible = false

        }
    }

    private fun showRequestInProgress() {
        binding.apply {
            loading.isVisible = true

            noInternetStub.isVisible = false
            countriesRecyclerView.isVisible = false
            serverErrorStub.isVisible = false

        }
    }

    private fun showServerError() {
        binding.apply {
            serverErrorStub.isVisible = true

            countriesRecyclerView.isVisible = false
            noInternetStub.isVisible = false
            loading.isVisible = false

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
