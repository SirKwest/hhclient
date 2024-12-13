package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.searchField.setOnFocusChangeListener { _, isFocused ->
            if (isFocused && binding.searchField.text!!.isNotEmpty()) {
                viewModel.search(binding.searchField.text.toString())
            }
        }

        binding.searchField.requestFocus()

        val searchFieldTextWatcher = object : TextWatcher {
            @Suppress("detekt.EmptyFunctionBlock")
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.search(p0.toString())
            }
            @Suppress("detekt.EmptyFunctionBlock")
            override fun afterTextChanged(p0: Editable?) {
            }

        }
        binding.searchField.addTextChangedListener(searchFieldTextWatcher)
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
                    context?.getString(R.string.no_such_vacancies),
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
        }
    }
}
