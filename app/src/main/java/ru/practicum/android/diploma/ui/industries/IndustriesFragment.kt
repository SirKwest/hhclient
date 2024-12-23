package ru.practicum.android.diploma.ui.industries

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustriesBinding

class IndustriesFragment : Fragment() {
    private var _binding: FragmentIndustriesBinding? = null
    private val binding get() = _binding!!
    private val industriesListAdapter = IndustriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        industriesListAdapter.onItemClickListener =
            IndustriesListAdapter.OnItemClickListener { binding.selectButton.isVisible = true }
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            industriesRecyclerView.adapter = industriesListAdapter
        }
        initSearchEditText()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEditText() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
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
