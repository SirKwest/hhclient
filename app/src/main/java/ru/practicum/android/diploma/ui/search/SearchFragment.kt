package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.presentation.SearchFragmentViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val viewModel: SearchFragmentViewModel by viewModel()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeData().observe(viewLifecycleOwner) { state ->
            processingChangedScreenState(state)
        }

        binding.searchEditText.setOnFocusChangeListener { _, isFocused ->
            if (isFocused && binding.searchEditText.text!!.isNotEmpty()) {
                viewModel.search(binding.searchEditText.text.toString())
            }
        }

        binding.searchEditText.requestFocus()

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.search(text.toString())
            }
        }

        binding.filterIcon.setOnClickListener{
            viewModel.addFilter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Created only for prototype testing purposes.
     * Should be refactored in issue #21
     */
    private fun processingChangedScreenState(newState: SearchFragmentState) {
        when (newState) {
            SearchFragmentState.Default -> { Toast.makeText(context, "Default", Toast.LENGTH_LONG).show() }
            SearchFragmentState.EmptyResults -> {
                Toast.makeText(
                    context,
                    context?.getString(R.string.no_such_vacancy),
                    Toast.LENGTH_LONG
                ).show()
            }
            SearchFragmentState.LoadingNewPageOfResults -> {
                Toast.makeText(context, "LoadingNewPage", Toast.LENGTH_LONG).show()
            }
            SearchFragmentState.NoInternetAccess -> {
                Toast.makeText(context, "NoInternet", Toast.LENGTH_LONG).show()
            }
            SearchFragmentState.RequestInProgress -> {
                Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
            }
            SearchFragmentState.ServerError -> {
                Toast.makeText(context, "ServerError", Toast.LENGTH_LONG).show()
            }
            is SearchFragmentState.ShowingResults -> {
                Toast.makeText(
                    context,
                    context?.resources?.getQuantityString(
                        R.plurals.vacancies_found,
                        newState.total,
                        newState.total,
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }

            is SearchFragmentState.FilterState -> if (newState.isActive) {
                binding.filterIcon.setImageResource(R.drawable.icon_filter_inactive)
            } else {
                binding.filterIcon.setImageResource(R.drawable.icon_filter_active)
            }
        }
    }
}
