package app.sonlabs.myweatherforecast.domain;

import app.sonlabs.myweatherforecast.data.Coord
import kotlinx.coroutines.flow.Flow;

interface ILocationManager {
     suspend fun getLastLocation(): Flow<Coord>
}
