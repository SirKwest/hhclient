package ru.practicum.android.diploma.ui.details

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.VacancyDetailsFragmentState
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.util.SalaryDescriptionBuilder

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
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
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
                        viewModel.updateFavoriteStatus()
                        true
                    }

                    else -> false
                }
            }
        }

        viewModel.observeScreenState().observe(viewLifecycleOwner, ::processState)
        viewModel.observeFavoriteState().observe(viewLifecycleOwner, ::processFavoriteState)
    }

    private fun processFavoriteState(state: Boolean) {
        if (state) {
            binding.toolbar.menu[1].setIcon(R.drawable.icon_favorite_active)
        } else {
            binding.toolbar.menu[1].setIcon(R.drawable.icon_favorite_black_inactive)
        }
    }

    private fun processState(state: VacancyDetailsFragmentState) {
        when (state) {
            is VacancyDetailsFragmentState.ShowingResults -> showResults(state.vacancy)
            is VacancyDetailsFragmentState.EmptyResults -> showEmptyResults()
            is VacancyDetailsFragmentState.NoInternetAccess -> showNoInternet()
            is VacancyDetailsFragmentState.RequestInProgress -> showRequestInProgress()
            is VacancyDetailsFragmentState.ServerError -> showServerError()
        }
    }

    private fun showResults(vacancy: Vacancy) {
        fillContent(vacancy)
        binding.apply {
            content.isVisible = true

            notFoundStub.isVisible = false
            serverErrorStub.isVisible = false
            loading.isVisible = false
        }
    }

    private fun fillContent(vacancy: Vacancy) {
        binding.apply {
            vacancyName.text = vacancy.name
            salary.text = SalaryDescriptionBuilder(requireContext()).build(
                vacancy.salaryLow,
                vacancy.salaryHigh,
                vacancy.currency
            )
            employerName.text = vacancy.employer
            employerArea.text = vacancy.area
            Glide.with(requireContext())
                .load(vacancy.logo)
                .placeholder(R.drawable.icon_placeholder_logo)
                .fitCenter()
                .into(employerLogo)

            if (vacancy.experience == null) {
                experienceGroup.isVisible = false
            } else {
                experienceGroup.isVisible = true
                experience.text = vacancy.experience
            }

            val subinfoText = buildSubinfo(vacancy.employment, vacancy.schedule)
            if (subinfoText == null) {
                subinfo.isVisible = false
            } else {
                subinfo.isVisible = true
                subinfo.text = subinfoText
            }
            description.text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT)

            val keySkillsText = buildKeySkills(vacancy.keySkills)
            if (keySkillsText == null) {
                keySkillsGroup.isVisible = false
            } else {
                keySkillsGroup.isVisible = true
                keySkills.text = keySkillsText
            }
        }
    }

    private fun showEmptyResults() {
        binding.apply {
            notFoundStub.isVisible = true

            content.isVisible = false
            serverErrorStub.isVisible = false
            noInternetStub.isVisible = false
            loading.isVisible = false

        }
    }

    private fun showNoInternet() {
        binding.apply {
            noInternetStub.isVisible = true

            content.isVisible = false
            serverErrorStub.isVisible = false
            notFoundStub.isVisible = false
            loading.isVisible = false
        }
    }

    private fun showRequestInProgress() {
        binding.apply {
            loading.isVisible = true

            content.isVisible = false
            serverErrorStub.isVisible = false
            notFoundStub.isVisible = false
            noInternetStub.isVisible = false
        }
    }

    private fun showServerError() {
        binding.apply {
            serverErrorStub.isVisible = true

            loading.isVisible = false
            content.isVisible = false
            notFoundStub.isVisible = false
            noInternetStub.isVisible = false
        }
    }

    private fun buildSubinfo(
        employment: String?,
        schedule: String?
    ): String? {
        if (employment == null && schedule == null) return null
        var result = employment ?: ""
        schedule?.let {
            result += ", $it"
        }

        return result
    }

    private fun buildKeySkills(keySkills: List<String>): String? {
        if (keySkills.isEmpty()) return null
        return keySkills.joinToString(
            separator = "\n•    ",
            prefix = "•    ",
        )
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
