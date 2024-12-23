package ru.practicum.android.diploma.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountriesBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.CountriesFragmentState
import ru.practicum.android.diploma.presentation.CountriesViewModel

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

    }

    private fun showNoInternet() {

    }

    private fun showRequestInProgress() {

    }

    private fun showServerError() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
