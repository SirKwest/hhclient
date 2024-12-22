package ru.practicum.android.diploma.ui.industries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentIndustriesBinding
import ru.practicum.android.diploma.domain.models.Industry

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
        industriesListAdapter.industries = (1..10).map { ind -> Industry(ind.toString(), "Stub") } // Stub
        binding.apply {
            industriesRecyclerView.adapter = industriesListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
