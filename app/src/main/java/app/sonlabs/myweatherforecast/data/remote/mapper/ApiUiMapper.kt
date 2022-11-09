package app.sonlabs.myweatherforecast.data.remote.mapper

import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import app.sonlabs.myweatherforecast.data.remote.Mapper

class ApiUiMapper : Mapper<Forecast, Forecast> {
    override fun map(data: Forecast): Forecast = data.run {
        val iconName = data.weather[0].icon
        val url = "http://openweathermap.org/img/wn/$iconName@4x.png"
        weather[0].icon = url
        this
    }
}

class ApiDetailUiMapper : Mapper<ForecastDetail, ForecastDetail> {
    override fun map(data: ForecastDetail): ForecastDetail = data.run {
        this.hourly.map { m ->
            val iconName = m.weather[0].icon
            val url = "http://openweathermap.org/img/wn/$iconName@2x.png"
            m.weather[0].icon = url
            m
        }
        this
    }
}