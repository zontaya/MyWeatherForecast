package app.sonlabs.myweatherforecast.ui.detail

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import app.sonlabs.myweatherforecast.R
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.data.remote.Current
import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import app.sonlabs.myweatherforecast.ui.main.units
import com.squareup.picasso.Picasso
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@BindingAdapter(value = ["details", "isMetric"])
fun addItems(recyclerView: RecyclerView, details: UiResponse<ForecastDetail>, isMetric: Boolean) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = DetailAdapter(isMetric)
    }
    (recyclerView.adapter as DetailAdapter).apply {
        if (details is UiResponse.Success) {
            submitList(details.data.hourly)
        }
    }
}

@BindingAdapter(
    value = ["temp", "isMetric"], requireAll = true
)
fun setTemp(view: TextView, temp: Double, isMetric: Boolean) {
    val format = String.format(
        view.context.getString(R.string.temp),
        temp,
        isMetric.units()
    )
    view.text = format
}

@BindingAdapter(
    value = ["setTime"], requireAll = true
)
fun setTime(view: TextView, time: Long) {
    val dt = convertDate(time)
    var minute = "${dt.minute}"
    var hour = "${dt.hour}"

    if (dt.minute < 10) {
        minute = "0${dt.minute}"
    }
    if (dt.hour < 10) {
        hour = "0${dt.hour}"
    }
    view.text = String.format(
        view.context.getString(R.string.temp_detail_dt),
        dt.date.toString(), hour, minute
    )
}

@BindingAdapter(
    value = ["tempDes"], requireAll = true
)
fun tempDes(view: TextView, current: Current) {
    view.text = current.weather[0].description
}

@BindingAdapter(
    value = ["setIcon"], requireAll = true
)
fun setIcon(view: ImageView, current: Current) {
    val iconName = current.weather[0].icon
    Picasso.get().load(iconName).into(view)
}

private fun convertDate(dateInMilliseconds: Long): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    val currentMoment = Instant.fromEpochSeconds(dateInMilliseconds)
    return currentMoment.toLocalDateTime(tz)
}