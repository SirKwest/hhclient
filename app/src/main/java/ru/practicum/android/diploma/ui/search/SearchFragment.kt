package ru.practicum.android.diploma.ui.search

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.content.ContextCompat
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

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val viewModel: SearchFragmentViewModel by viewModel()

    private var vacanciesAdapter = VacancyListAdapter(mutableListOf())

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processingChangedScreenState(SearchFragmentState.Default)

        viewModel.observeData().observe(viewLifecycleOwner) { state ->
            processingChangedScreenState(state)
        }

        viewModel.observeFilter().observe(viewLifecycleOwner) { filterState ->
            binding.filterIcon.setImageResource(
                if (filterState) {
                    R.drawable.icon_filter_active
                } else {
                    R.drawable.icon_filter_inactive
                }
            )
        }

        binding.filterIcon.setOnClickListener {
            viewModel.addFilter()
        }

        binding.searchEditText.setOnFocusChangeListener { _, isFocused ->
            if (isFocused && binding.searchEditText.text!!.isNotEmpty()) {
                viewModel.search(binding.searchEditText.text.toString())
            }
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val drawableEnd: Drawable?
            if (text?.isNotBlank() == true) {
                viewModel.search(text.toString())
                drawableEnd = ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete)
            } else {
                drawableEnd = ContextCompat.getDrawable(requireContext(), R.drawable.icon_search)
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
                    Bundle().apply { putString(VACANCY_ID_KEY, item.id) }
                )
            }
        })
        binding.vacancyRecyclerView.adapter = vacanciesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun processingChangedScreenState(newState: SearchFragmentState) {
        when (newState) {
            SearchFragmentState.Default -> {
                binding.infoImageView.setImageResource(R.drawable.start_info_image)
                binding.infoImageView.isVisible = true

                binding.infoTextView.isVisible = false
                binding.vacancyRecyclerView.isVisible = false
                binding.vacancyCountTextView.isVisible = false
                binding.progressBar.isVisible = false
                binding.progressBarForPageLoading.isVisible = false
            }
            SearchFragmentState.EmptyResults -> {
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
            SearchFragmentState.LoadingNewPageOfResults -> {
                binding.progressBarForPageLoading.isVisible = true
                binding.vacancyRecyclerView.isVisible = true
                binding.vacancyCountTextView.isVisible = true
                binding.vacancyRecyclerView.scrollToPosition(vacanciesAdapter.itemCount - 1)

                binding.infoImageView.isVisible = false
                binding.infoTextView.isVisible = false
                binding.progressBar.isVisible = false
            }
            SearchFragmentState.NoInternetAccess -> {
                binding.infoImageView.setImageResource(R.drawable.no_internet_info_image)
                binding.infoImageView.isVisible = true
                binding.infoTextView.setText(R.string.no_internet)
                binding.infoTextView.isVisible = true

                binding.vacancyRecyclerView.isVisible = false
                binding.vacancyCountTextView.isVisible = false
                binding.progressBar.isVisible = false
                binding.progressBarForPageLoading.isVisible = false
            }
            SearchFragmentState.RequestInProgress -> {
                binding.progressBar.isVisible = true

                binding.infoImageView.isVisible = false
                binding.infoTextView.isVisible = false
                binding.vacancyRecyclerView.isVisible = false
                binding.vacancyCountTextView.isVisible = false
                binding.progressBarForPageLoading.isVisible = false
            }
            SearchFragmentState.ServerError -> {
                binding.infoImageView.setImageResource(R.drawable.server_error)
                binding.infoImageView.isVisible = true
                binding.infoTextView.setText(R.string.server_error)
                binding.infoTextView.isVisible = true

                binding.vacancyRecyclerView.isVisible = false
                binding.vacancyCountTextView.isVisible = false
                binding.progressBar.isVisible = false
                binding.progressBarForPageLoading.isVisible = false
            }
            is SearchFragmentState.ShowingResults -> {
                binding.vacancyCountTextView.isVisible = true
                binding.vacancyRecyclerView.isVisible = true
                vacanciesAdapter.resetData(newState.vacancies)
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
    }

    companion object {
        const val VACANCY_ID_KEY = "VACANCY_ID_KEY"
    }
}
