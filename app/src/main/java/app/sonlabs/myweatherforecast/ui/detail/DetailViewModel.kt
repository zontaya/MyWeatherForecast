package app.sonlabs.myweatherforecast.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import app.sonlabs.myweatherforecast.domain.GetDailyParams
import app.sonlabs.myweatherforecast.domain.usecase.GetDailyUseCase
import app.sonlabs.myweatherforecast.util.Constants
import app.sonlabs.myweatherforecast.util.Constants.KEY_DETAIL
import app.sonlabs.myweatherforecast.util.Constants.UNITS_IMPERIAL
import app.sonlabs.myweatherforecast.util.Constants.UNITS_METRIC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getDailyUseCase: GetDailyUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val detail = MutableStateFlow<UiResponse<ForecastDetail>>(UiResponse.Idle)
    val units = MutableStateFlow(true)

    init {
        getTemp()
    }

    fun getDetail(lat: Double, lon: Double, isMetric: Boolean) = viewModelScope.launch {
        detail.emit(UiResponse.Loading)
        units.emit(isMetric)
        val param = GetDailyParams(
            lat,
            lon,
            if (isMetric) UNITS_METRIC else UNITS_IMPERIAL
        )
        getDailyUseCase.execute(param)
            .catch { e ->
                detail.emit(UiResponse.Error(e.message))
            }
            .collect {
                savedStateHandle[KEY_DETAIL] = it
                detail.emit(UiResponse.Success(it))
            }
    }

    private fun getTemp() = viewModelScope.launch {
        val temp: ForecastDetail? = savedStateHandle[KEY_DETAIL]
        if (temp != null) {
            detail.emit(UiResponse.Success(temp))
        }
    }
}