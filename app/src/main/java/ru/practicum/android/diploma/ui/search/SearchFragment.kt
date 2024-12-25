package ru.practicum.android.diploma.ui.search

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.presentation.SearchFragmentState
import ru.practicum.android.diploma.presentation.SearchFragmentViewModel
import ru.practicum.android.diploma.ui.details.VacancyDetailsFragment
import java.net.HttpURLConnection

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val viewModel: SearchFragmentViewModel by viewModel()

    private var vacanciesAdapter = VacancyListAdapter(mutableListOf())

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        processingChangedScreenState(SearchFragmentState.Default)
        applyingFunctionsToLayoutItems()
        settingListeners()
        settingRecyclerViewAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkFilterValuesExistence()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun applyingFunctionsToLayoutItems() {
        binding.filterIcon.setOnClickListener {
            findNavController().navigate(R.id.action_search_to_filter_settings_fragment)
        }
        binding.searchEditText.setOnFocusChangeListener { _, isFocused ->
            if (isFocused && binding.searchEditText.text!!.isNotEmpty()) {
                viewModel.search(binding.searchEditText.text.toString())
            }
        }
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val drawableEnd: Drawable? = if (text?.isNotBlank() == true) {
                viewModel.search(text.toString())
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
                    processingChangedScreenState(SearchFragmentState.Default)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun settingListeners() {
        viewModel.observeData().observe(viewLifecycleOwner) { state ->
            processingChangedScreenState(state)
        }

        viewModel.observeFilter().observe(viewLifecycleOwner) { filterSaved ->
            binding.filterIcon.setImageResource(
                if (filterSaved) {
                    R.drawable.icon_filter_active
                } else {
                    R.drawable.icon_filter_inactive
                }
            )
        }

        viewModel.observeErrorMessage().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Toast.makeText(requireContext(), getString(R.string.check_internet_connection), Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_happened), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun settingRecyclerViewAdapter() {
        binding.vacancyRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val lastVisibleItemPosition =
                        (binding.vacancyRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (lastVisibleItemPosition >= vacanciesAdapter.itemCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
        vacanciesAdapter = VacancyListAdapter(mutableListOf())
        vacanciesAdapter.setOnItemClickListener(object : VacancyListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = vacanciesAdapter.getItemByPosition(position)
                findNavController().navigate(
                    R.id.vacancy_details_fragment,
                    VacancyDetailsFragment.createBundleOf(item.id)
                )
            }
        })
        binding.vacancyRecyclerView.adapter = vacanciesAdapter
    }

    private fun processingChangedScreenState(newState: SearchFragmentState) {
        when (newState) {
            SearchFragmentState.Default -> showDefaultScreenState()
            SearchFragmentState.EmptyResults -> showEmptyResultsScreen()
            SearchFragmentState.LoadingNewPageOfResults -> showLoadingNewPageScreen()
            SearchFragmentState.NoInternetAccess -> showNoInternetAccessScreen()
            SearchFragmentState.RequestInProgress -> showRequestInProgressScreen()
            SearchFragmentState.ServerError -> showServerErrorScreen()
            is SearchFragmentState.ShowingResults -> showResultsScreen(newState)
        }
    }
    private fun showDefaultScreenState() {
        binding.infoImageView.setImageResource(R.drawable.start_info_image)
        binding.infoImageView.isVisible = true

        binding.infoTextView.isVisible = false
        binding.vacancyRecyclerView.isVisible = false
        binding.vacancyCountTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarForPageLoading.isVisible = false
    }

    private fun showEmptyResultsScreen() {
        binding.infoImageView.setImageResource(R.drawable.no_vacancy_image)
        binding.infoImageView.isVisible = true
        binding.infoTextView.setText(R.string.cant_get_vacancy)
        binding.infoTextView.isVisible = true
        binding.vacancyCountTextView.isVisible = true
        binding.vacancyCountTextView.setText(R.string.no_such_vacancy)

        binding.vacancyRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarForPageLoading.isVisible = false
    }

    private fun showLoadingNewPageScreen() {
        binding.progressBarForPageLoading.isVisible = true
        binding.vacancyRecyclerView.isVisible = true
        binding.vacancyCountTextView.isVisible = true
        binding.vacancyRecyclerView.scrollToPosition(vacanciesAdapter.itemCount - 1)

        binding.infoImageView.isVisible = false
        binding.infoTextView.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showNoInternetAccessScreen() {
        binding.infoImageView.setImageResource(R.drawable.no_internet_info_image)
        binding.infoImageView.isVisible = true
        binding.infoTextView.setText(R.string.no_internet)
        binding.infoTextView.isVisible = true

        binding.vacancyRecyclerView.isVisible = false
        binding.vacancyCountTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarForPageLoading.isVisible = false
    }

    private fun showRequestInProgressScreen() {
        binding.progressBar.isVisible = true

        binding.infoImageView.isVisible = false
        binding.infoTextView.isVisible = false
        binding.vacancyRecyclerView.isVisible = false
        binding.vacancyCountTextView.isVisible = false
        binding.progressBarForPageLoading.isVisible = false
    }

    private fun showServerErrorScreen() {
        binding.infoImageView.setImageResource(R.drawable.server_error)
        binding.infoImageView.isVisible = true
        binding.infoTextView.setText(R.string.server_error)
        binding.infoTextView.isVisible = true

        binding.vacancyRecyclerView.isVisible = false
        binding.vacancyCountTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarForPageLoading.isVisible = false
    }

    private fun showResultsScreen(newState: SearchFragmentState.ShowingResults) {
        binding.vacancyCountTextView.isVisible = true
        binding.vacancyRecyclerView.isVisible = true
        vacanciesAdapter.resetDataTo(newState.vacancies)
        vacanciesAdapter.notifyDataSetChanged()
        binding.vacancyCountTextView.text = context?.resources?.getQuantityString(
            R.plurals.vacancies_found,
            newState.total,
            newState.total,
        )

        binding.infoTextView.isVisible = false
        binding.infoImageView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarForPageLoading.isVisible = false
    }
}
