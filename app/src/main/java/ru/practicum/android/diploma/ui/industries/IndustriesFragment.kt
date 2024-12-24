package ru.practicum.android.diploma.ui.industries

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustriesBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.IndustriesFragmentState
import ru.practicum.android.diploma.presentation.IndustriesViewModel

class IndustriesFragment : Fragment() {
    private var _binding: FragmentIndustriesBinding? = null
    private val binding get() = _binding!!
    private val industriesListAdapter = IndustriesListAdapter()
    private val viewModel: IndustriesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        industriesListAdapter.onItemClickListener =
            IndustriesListAdapter.OnItemClickListener {
                binding.selectButton.isVisible = true
                binding.industriesRecyclerView.updatePadding(bottom = 84.toPx(resources))
            }
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            industriesRecyclerView.adapter = industriesListAdapter
            selectButton.setOnClickListener {
                // использовать industriesListAdapter.getSelectedIndustry() для получения селекта
                findNavController().navigateUp()
            }
        }
        initSearchEditText()
        viewModel.observeScreenState().observe(viewLifecycleOwner, ::processState)
    }

    private fun processState(state: IndustriesFragmentState) {
        when (state) {
            is IndustriesFragmentState.ShowingResults -> showResults(state.industries)
            is IndustriesFragmentState.NoInternetAccess -> showNoInternet()
            is IndustriesFragmentState.RequestInProgress -> showRequestInProgress()
            is IndustriesFragmentState.ServerError -> showServerError()
        }
    }

    private fun showResults(industries: List<Industry>) {
        binding.apply {
            content.isVisible = true
            industriesListAdapter.industries = industries
            industriesRecyclerView.scrollToPosition(0)

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

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEditText() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.filter(text?.toString().orEmpty())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Int.toPx(resources: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        resources.displayMetrics
    ).toInt()
}
