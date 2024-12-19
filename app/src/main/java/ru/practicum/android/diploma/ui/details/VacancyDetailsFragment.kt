package ru.practicum.android.diploma.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

class VacancyDetailsFragment : Fragment() {
    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyDetailsViewModel by viewModel {
        val id = requireArguments().getString(VACANCY_ID_KEY)
        parametersOf(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share -> {
                        viewModel.shareVacancy()
                        true
                    }

                    R.id.favorite -> {
                        viewModel.updateFavorite()
                        true
                    }

                    else -> false
                }
            }
        }

        viewModel // just a trigger
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VACANCY_ID_KEY = "VACANCY_ID_KEY"

        fun createBundleOf(id: String): Bundle {
            return bundleOf(VACANCY_ID_KEY to id)
        }
    }
}
