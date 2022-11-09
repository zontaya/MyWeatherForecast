package app.sonlabs.myweatherforecast.data.location

import android.annotation.SuppressLint
import android.content.Context
import app.sonlabs.myweatherforecast.data.Coord
import app.sonlabs.myweatherforecast.domain.ILocationManager

import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ForecastLocationService(private val context: Context) : ILocationManager {
    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation() = flow {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val location = suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location == null)
                        continuation.resumeWithException(Exception("Location not available"))
                    else
                        continuation.resume(Coord(location.longitude, location.latitude))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
        emit(location)
    }
}
