package ru.practicum.android.diploma.ui.regions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionsBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.RegionsFragmentState
import ru.practicum.android.diploma.presentation.RegionsViewModel
import ru.practicum.android.diploma.ui.location.LocationListAdapter
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
    private val regionsListAdapter = LocationListAdapter<Region>()

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
        regionsListAdapter.onItemClickListener = LocationListAdapter.OnItemClickListener { region ->
            setFragmentResult(
                WorkLocationFragment.REGION_RESULT_KEY,
                bundleOf(WorkLocationFragment.REGION_DATA_KEY to region)
            )
            findNavController().navigateUp()
        }
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            regionsRecyclerView.adapter = regionsListAdapter
        }
        initSearchEditText()
        viewModel.observeScreenState().observe(viewLifecycleOwner, ::processState)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEditText() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
            val drawableEnd: Drawable? = if (text?.isNotBlank() == true) {
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_search)
            }
            binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawableEnd,
                null
            )
        }
        binding.searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.searchEditText.compoundDrawablesRelative[2]
                val position = binding.searchEditText.width -
                    binding.searchEditText.paddingEnd - drawableEnd.bounds.width()
                if (drawableEnd != null && event.rawX >= position) {
                    binding.searchEditText.text?.clear()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun processState(state: RegionsFragmentState) {
        when (state) {
            is RegionsFragmentState.ShowingResults -> showResults(state.regions)
            is RegionsFragmentState.NoInternetAccess -> showNoInternet()
            is RegionsFragmentState.RequestInProgress -> showRequestInProgress()
            is RegionsFragmentState.EmptyResults -> showEmptyResults()
            is RegionsFragmentState.ServerError -> showServerError()
        }
    }

    private fun showResults(regions: List<Region>) {
        binding.apply {
            regionsRecyclerView.isVisible = true
            content.isVisible = true
            regionsListAdapter.locations = regions
            regionsRecyclerView.scrollToPosition(0)

            notFoundStub.isVisible = false
            noInternetStub.isVisible = false
            serverErrorStub.isVisible = false
            loading.isVisible = false
        }
    }

    private fun showEmptyResults() {
        binding.apply {
            notFoundStub.isVisible = true
            content.isVisible = true

            regionsRecyclerView.isVisible = false
            noInternetStub.isVisible = false
            serverErrorStub.isVisible = false
            loading.isVisible = false

        }
    }

    private fun showNoInternet() {
        binding.apply {
            noInternetStub.isVisible = true

            content.isVisible = false
            serverErrorStub.isVisible = false
            loading.isVisible = false

        }
    }

    private fun showRequestInProgress() {
        binding.apply {
            loading.isVisible = true

            noInternetStub.isVisible = false
            content.isVisible = false
            serverErrorStub.isVisible = false

        }
    }

    private fun showServerError() {
        binding.apply {
            serverErrorStub.isVisible = true

            content.isVisible = false
            noInternetStub.isVisible = false
            loading.isVisible = false

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
