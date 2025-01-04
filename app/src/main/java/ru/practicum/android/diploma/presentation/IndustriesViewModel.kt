package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.IndustriesResource
import ru.practicum.android.diploma.domain.models.Industry
import java.net.HttpURLConnection

class IndustriesViewModel(
    private val industriesInteractor: IndustriesInteractor,
    private val filtersInteractor: FilterInteractor
) : ViewModel() {

    private val screenState = MutableLiveData<IndustriesFragmentState>()
    fun observeScreenState(): LiveData<IndustriesFragmentState> = screenState
    private var baseIndustries: List<Industry>? = null

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            screenState.postValue(IndustriesFragmentState.RequestInProgress)
            industriesInteractor.getIndustries().collect { resource ->
                when (resource) {
                    is IndustriesResource.Success -> {
                        val industries = resource.industries.flatMap { it.industries }
                        baseIndustries = industries
                        screenState.postValue(IndustriesFragmentState.ShowingResults(industries))
                    }

                    is IndustriesResource.Error -> {
                        when (resource.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            HttpURLConnection.HTTP_FORBIDDEN,
                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                screenState.postValue(IndustriesFragmentState.ServerError)
                            }

                            else -> {
                                screenState.postValue(IndustriesFragmentState.NoInternetAccess)
                            }
                        }
                    }
                }
            }
        }
    }

    fun filter(query: String) {
        baseIndustries?.let { industries ->
            val filteredList = industries.filter { industry -> industry.name!!.lowercase().contains(query.lowercase()) }
            if (filteredList.isEmpty()) {
                screenState.value = IndustriesFragmentState.EmptyResults
            } else {
                screenState.value = IndustriesFragmentState.ShowingResults(filteredList)
            }
        }
    }

    fun saveValue(item: Industry?) {
        filtersInteractor.saveIndustry(item!!)
    }
}
