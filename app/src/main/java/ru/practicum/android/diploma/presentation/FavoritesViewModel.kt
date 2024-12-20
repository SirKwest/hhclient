package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource

class FavoritesViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {
    private val screenState = MutableLiveData<FavoritesFragmentState>()

    fun observeScreenState(): LiveData<FavoritesFragmentState> = screenState

    fun loadFavoriteVacanciesFromDB() {
        viewModelScope.launch {
            favoriteInteractor.getFavoriteVacancies().collect { result ->
                when (result) {
                    is VacancyFromDatabaseResource.Success -> {
                        if (result.records.isEmpty()) {
                            screenState.postValue(FavoritesFragmentState.EmptyResults)
                        } else {
                            screenState.postValue(FavoritesFragmentState.ShowResults(result.records))
                        }
                    }
                    is VacancyFromDatabaseResource.Error -> {
                        screenState.postValue(FavoritesFragmentState.Error)
                    }
                }
            }
        }
    }
}
