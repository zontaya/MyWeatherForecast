package app.sonlabs.myweatherforecast.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sonlabs.myweatherforecast.data.Coord
import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.domain.SearchCityByLocationParams
import app.sonlabs.myweatherforecast.domain.SearchCityParams
import app.sonlabs.myweatherforecast.domain.usecase.GetLocationUseCase
import app.sonlabs.myweatherforecast.domain.usecase.SearchCityByLocationUseCase
import app.sonlabs.myweatherforecast.domain.usecase.SearchCityUseCase
import app.sonlabs.myweatherforecast.util.Constants.KEY_FORECAST
import app.sonlabs.myweatherforecast.util.Constants.KEY_UNIT
import app.sonlabs.myweatherforecast.util.Constants.UNITS_IMPERIAL
import app.sonlabs.myweatherforecast.util.Constants.UNITS_METRIC
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val searchCityUseCase: SearchCityUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val searchCityByLocationUseCase: SearchCityByLocationUseCase
) : ViewModel() {

    val forecast = MutableStateFlow<UiResponse<Forecast>>(UiResponse.Idle)
    val units = MutableStateFlow(true)

    init {
        getTemp()
    }

    fun setUnits() = viewModelScope.launch {
        units.emit(units.value.not())
        savedStateHandle[KEY_UNIT] = units.value
    }

    fun search(name: String) = viewModelScope.launch {
        forecast.emit(UiResponse.Loading)
        val param = SearchCityParams(
            name = name,
            units = if (units.value) UNITS_METRIC else UNITS_IMPERIAL
        )
        searchCityUseCase.execute(param)
            .catch { e ->
                forecast.emit(UiResponse.Error(e.message))
            }
            .collect {
                saveAndDisplay(it)
            }
    }

    fun getData(): Forecast? {
        return savedStateHandle[KEY_FORECAST]
    }

    fun getLocation() = viewModelScope.launch(IO) {
        getLocationUseCase.execute()
            .collect {
                searchByLocation(it)
            }
    }

    private suspend fun searchByLocation(it: Coord) = viewModelScope.launch {
        val temp: Forecast? = savedStateHandle[KEY_FORECAST]
        if (temp == null) {
            val param = SearchCityByLocationParams(
                location = it,
                units = if (units.value) UNITS_METRIC else UNITS_IMPERIAL
            )
            searchCityByLocationUseCase.execute(param)
                .collect {
                    saveAndDisplay(it)
                }
        }
    }

    private suspend fun saveAndDisplay(it: Forecast) {
        savedStateHandle[KEY_FORECAST] = it
        forecast.emit(UiResponse.Success(it))
    }

    private fun getTemp() = viewModelScope.launch {
        val temp: Forecast? = savedStateHandle[KEY_FORECAST]
        val unit = savedStateHandle[KEY_UNIT] ?: true
        temp?.let {
            forecast.emit(UiResponse.Success(temp))
        }
        units.emit(unit)
    }
}
