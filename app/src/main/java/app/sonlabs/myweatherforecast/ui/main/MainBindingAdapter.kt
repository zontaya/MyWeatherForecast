package app.sonlabs.myweatherforecast.ui.main

import android.widget.TextView
import androidx.databinding.BindingAdapter
import app.sonlabs.myweatherforecast.R
import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.data.UiResponse
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val units = listOf("Metric : °C", "Imperial : °F")

@BindingAdapter(
    value = ["setCityName"], requireAll = true
)
fun setCityName(view: TextInputEditText, forecast: UiResponse<Forecast>) {
    if (forecast is UiResponse.Success) {
        view.setText(forecast.data.name, TextView.BufferType.EDITABLE)
    }
}

@BindingAdapter(
    value = ["setCountry"], requireAll = true
)
fun setCountry(view: TextView, forecast: UiResponse<Forecast>) {
    if (forecast is UiResponse.Success) {
        val timeRefreshStr = String.format(
            view.context.getString(R.string.temp_title),
            forecast.data.name,
            forecast.data.sys.country
        )
        view.text = timeRefreshStr
    }
}

@BindingAdapter(
    value = ["setHumidity"], requireAll = true
)
fun setHumidity(view: TextView, forecast: UiResponse<Forecast>) {
    if (forecast is UiResponse.Success) {
        val humidity = String.format(
            view.context.getString(R.string.temp_humidity),
            forecast.data.main.humidity, "%"
        )
        view.text = humidity
    }
}

@BindingAdapter(
    value = ["setIcon"], requireAll = true
)
fun setIcon(view: ShapeableImageView, forecast: UiResponse<Forecast>) {
    if (forecast is UiResponse.Success) {
        if (forecast.data.weather.isNotEmpty()) {
            val iconName = forecast.data.weather[0].icon
            Picasso.get().load(iconName).into(view)
        }
    }
}

@BindingAdapter(
    value = ["isMetric"], requireAll = true
)
fun isMetric(view: ExtendedFloatingActionButton, isMetric: Boolean) {
    view.text = if (isMetric) {
        units[1]
    } else {
        units[0]
    }
}

@BindingAdapter(
    value = ["forecastTemp", "isMetric"], requireAll = true
)
fun setTemp(view: TextView, forecast: UiResponse<Forecast>, isMetric: Boolean) {
    if (forecast is UiResponse.Success) {
        val temp = String.format(
            view.context.getString(R.string.temp),
            forecast.data.main.temp,
            isMetric.units()
        )
        view.text = temp
    }
}

@BindingAdapter(
    value = ["forecastFeel", "isMetric"], requireAll = true
)
fun setFeel(view: TextView, forecast: UiResponse<Forecast>, isMetric: Boolean) {
    if (forecast is UiResponse.Success) {
        val feel = String.format(
            view.context.getString(R.string.temp_feels_like),
            forecast.data.main.feels_like,
            isMetric.units()
        )
        view.text = feel
    }
}

@BindingAdapter(
    value = ["setDatetime"], requireAll = true
)
fun setDatetime(view: TextView, forecast: UiResponse<Forecast>) {
    if (forecast is UiResponse.Success) {
        val dt = view.convertDate(forecast.data.dt)
        view.text = dt
    }
}

private fun TextView.convertDate(dateInMilliseconds: Long): String {
    val tz = TimeZone.currentSystemDefault()
    val currentMoment = Instant.fromEpochSeconds(dateInMilliseconds)
    val dt = currentMoment.toLocalDateTime(tz)
    var minute = "${dt.minute}"

    if (dt.minute < 10) {
        minute = "0${dt.minute}"
    }
    return String.format(
        context.getString(R.string.temp_dt),
        dt.hour, minute
    )
}

fun Boolean.units() = if (this) "°C" else "°F"
