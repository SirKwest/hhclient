package ru.practicum.android.diploma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.presentation.FavoritesFragmentState
import ru.practicum.android.diploma.presentation.FavoritesViewModel
import ru.practicum.android.diploma.ui.details.VacancyDetailsFragment
import ru.practicum.android.diploma.ui.search.VacancyListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModel()

    private var vacanciesAdapter = VacancyListAdapter(mutableListOf())
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFavoriteVacanciesFromDB()

        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoritesFragmentState.EmptyResults -> {
                    binding.favoritesInfoLayout.isVisible = true
                    binding.favoritesInfoIv.setImageResource(R.drawable.empty_favorites)
                    binding.favoritesInfoTv.setText(R.string.list_is_empty)

                    binding.favoritesRv.isVisible = false
                }

                FavoritesFragmentState.Error -> {
                    binding.favoritesInfoLayout.isVisible = true
                    binding.favoritesInfoIv.setImageResource(R.drawable.no_vacancy_image)
                    binding.favoritesInfoTv.setText(R.string.cant_get_vacancy)

                    binding.favoritesRv.isVisible = false
                }

                is FavoritesFragmentState.ShowResults -> {
                    binding.favoritesRv.isVisible = true
                    vacanciesAdapter.resetDataTo(state.favorites)

                    binding.favoritesInfoLayout.isVisible = false

                }
            }
        }

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
        binding.favoritesRv.adapter = vacanciesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
