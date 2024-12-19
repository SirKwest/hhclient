package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyByIdResource
import java.net.HttpURLConnection

class VacancyDetailsViewModel(
    private val id: String,
    private val vacancyInteractor: VacancyInteractor,
    private val sharingInteractor: SharingInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private var vacancy: Vacancy? = null
    private var isFavorite: Boolean = false
    private val screenState = MutableLiveData<VacancyDetailsFragmentState>()
    fun observeState(): LiveData<VacancyDetailsFragmentState> = screenState

    init {
        loadData()
        checkVacancyIsFavorite()
    }

    private fun loadData() {
        viewModelScope.launch {
            screenState.postValue(VacancyDetailsFragmentState.RequestInProgress)
            vacancyInteractor.getVacancyById(id).collect { result ->
                when (result) {
                    is VacancyByIdResource.Success -> {
                        vacancy = result.item
                        screenState.postValue(VacancyDetailsFragmentState.ShowingResults(result.item))
                    }

                    is VacancyByIdResource.Error -> {
                        when (result.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            HttpURLConnection.HTTP_FORBIDDEN -> {
                                screenState.postValue(VacancyDetailsFragmentState.ServerError)
                            }

                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                screenState.postValue(VacancyDetailsFragmentState.EmptyResults)
                            }

                            else -> {
                                screenState.postValue(VacancyDetailsFragmentState.NoInternetAccess)
                            }
                        }
                    }
                }
            }
        }
    }

    fun shareVacancy() {
        (screenState.value as? VacancyDetailsFragmentState.ShowingResults).let { state ->
            state?.let {
                sharingInteractor.shareText(it.vacancy.url)
            }
        }
    }

    private fun checkVacancyIsFavorite() {
        val currentVacancy = vacancy ?: return
        viewModelScope.launch {
            favoriteInteractor.getFavoriteVacancyIds().collect { ids ->
                if (ids.contains(currentVacancy.id)) {
                    isFavorite = true
                }
            }
            screenState.postValue(VacancyDetailsFragmentState.IsFavorite(isFavorite))
        }
    }

    fun updateFavorite() {
        val currentVacancy = vacancy ?: return
        viewModelScope.launch {
            if (isFavorite) {
                favoriteInteractor.removeVacancyFromFavorite(currentVacancy)
                isFavorite = false
            } else {
                favoriteInteractor.insertVacancyToFavorite(currentVacancy)
                isFavorite = true
            }
            screenState.postValue(VacancyDetailsFragmentState.IsFavorite(isFavorite))
        }
    }
}
