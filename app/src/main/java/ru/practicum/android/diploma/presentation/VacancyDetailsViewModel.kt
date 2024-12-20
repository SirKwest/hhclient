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
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource
import java.net.HttpURLConnection

class VacancyDetailsViewModel(
    private val id: String,
    private val vacancyInteractor: VacancyInteractor,
    private val sharingInteractor: SharingInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private var vacancy: Vacancy? = null
    private var isFavorite: MutableLiveData<Boolean> = MutableLiveData(false)
    private val screenState = MutableLiveData<VacancyDetailsFragmentState>()
    fun observeScreenState(): LiveData<VacancyDetailsFragmentState> = screenState
    fun observeFavoriteState(): LiveData<Boolean> = isFavorite

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            screenState.postValue(VacancyDetailsFragmentState.RequestInProgress)
            vacancyInteractor.getVacancyById(id).collect { result ->
                when (result) {
                    is VacancyByIdResource.Success -> {
                        vacancy = result.item
                        screenState.postValue(VacancyDetailsFragmentState.ShowingResults(result.item))
                        isFavorite.postValue(result.item.isFavorite)
                    }

                    is VacancyByIdResource.Error -> {
                        when (result.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            HttpURLConnection.HTTP_FORBIDDEN -> {
                                screenState.postValue(VacancyDetailsFragmentState.ServerError)
                                favoriteInteractor.removeVacancyFromFavorite(id)
                            }

                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                screenState.postValue(VacancyDetailsFragmentState.EmptyResults)
                                favoriteInteractor.removeVacancyFromFavorite(id)
                            }

                            else -> {
                                favoriteInteractor.getFavoriteVacancyById(id).collect { result ->
                                    when (result) {
                                        is VacancyFromDatabaseResource.Success -> {
                                            screenState.postValue(VacancyDetailsFragmentState.ShowingResults(result.records))
                                            isFavorite.postValue(result.records.isFavorite)
                                        }
                                        is VacancyFromDatabaseResource.Error -> {
                                            screenState.postValue(VacancyDetailsFragmentState.NoInternetAccess)
                                        }
                                    }
                                }
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

    fun updateFavoriteStatus() {
        if (vacancy == null) {
            return
        }
        viewModelScope.launch {
            if (vacancy!!.isFavorite) {
                favoriteInteractor.removeVacancyFromFavorite(id)
                isFavorite.postValue(false)
            } else {
                favoriteInteractor.insertVacancyToFavorite(vacancy!!)
                isFavorite.postValue(true)
            }
        }
        vacancy = vacancy!!.copy(isFavorite = !vacancy!!.isFavorite)
    }
}
