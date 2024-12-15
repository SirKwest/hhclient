package ru.practicum.android.diploma.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

class VacancyDetailsFragment : Fragment() {
    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share -> {
                        viewModel.shareVacancy()
                        true
                    }

                    R.id.favorite -> {
                        true
                    }

                    else -> false
                }
            }

            email.setOnClickListener { viewModel.openEmail() }
            phoneNumber.setOnClickListener { viewModel.dial() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
