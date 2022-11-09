package app.sonlabs.myweatherforecast.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UseCaseOut<out OUT> {
    suspend fun execute(): Flow<OUT>
}
