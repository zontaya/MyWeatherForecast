package app.sonlabs.myweatherforecast.data.remote.repo

import app.sonlabs.myweatherforecast.data.remote.mapper.ApiDetailUiMapper
import app.sonlabs.myweatherforecast.data.remote.mapper.ApiUiMapper
import app.sonlabs.myweatherforecast.domain.*
import kotlinx.coroutines.flow.flow

class RepositoryImp(
    private val remote: IRemoteData,
    private val detailMapper: ApiDetailUiMapper,
    private val mapper: ApiUiMapper
) : IRepository {

    override suspend fun searchCity(param: SearchCityParams) = flow {
        val response = remote.searchCityFromApi(param.name, param.units)
        emit(mapper.map(response))
    }

    override suspend fun searchCityByLocation(param: SearchCityByLocationParams) = flow {
        val response = remote.searchCityByLocationFromApi(
            param.location.lat,
            param.location.lon,
            param.units
        )
        emit(mapper.map(response))
    }

    override suspend fun getDaily(param: GetDailyParams) = flow {
        val response = remote.getDailyFromApi(
            param.lat,
            param.lon,
            param.exclude,
            param.units
        )
        emit(detailMapper.map(response))
    }
}
