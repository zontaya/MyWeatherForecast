package app.sonlabs.myweatherforecast.domain.usecase

import app.sonlabs.myweatherforecast.data.Coord
import app.sonlabs.myweatherforecast.domain.ILocationManager
import kotlinx.coroutines.flow.Flow

class GetLocationUseCase(
    private val location: ILocationManager
) : UseCaseOut<Coord> {
    override suspend fun execute(): Flow<Coord> =
        location.getLastLocation()
}
