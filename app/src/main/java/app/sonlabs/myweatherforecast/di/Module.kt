package app.sonlabs.myweatherforecast.di

import androidx.lifecycle.SavedStateHandle
import app.sonlabs.myweatherforecast.data.location.ForecastLocationService
import app.sonlabs.myweatherforecast.data.remote.RemoteDataImp
import app.sonlabs.myweatherforecast.data.remote.mapper.ApiDetailUiMapper
import app.sonlabs.myweatherforecast.data.remote.mapper.ApiUiMapper
import app.sonlabs.myweatherforecast.data.remote.repo.RepositoryImp
import app.sonlabs.myweatherforecast.domain.ILocationManager
import app.sonlabs.myweatherforecast.domain.IRemoteData
import app.sonlabs.myweatherforecast.domain.IRepository
import app.sonlabs.myweatherforecast.domain.usecase.GetDailyUseCase
import app.sonlabs.myweatherforecast.domain.usecase.GetLocationUseCase
import app.sonlabs.myweatherforecast.domain.usecase.SearchCityByLocationUseCase
import app.sonlabs.myweatherforecast.domain.usecase.SearchCityUseCase
import app.sonlabs.myweatherforecast.ui.detail.DetailViewModel
import app.sonlabs.myweatherforecast.ui.main.MainViewModel
import app.sonlabs.myweatherforecast.util.Constants.API_KEY
import app.sonlabs.myweatherforecast.util.Constants.BASE_URL
import app.sonlabs.myweatherforecast.util.Constants.KEY_NAME
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { DetailViewModel(get(), get()) }
}

private val useCaseModule = module() {
    factory { GetLocationUseCase(get()) }
    factory { SearchCityByLocationUseCase(get()) }
    factory { SearchCityUseCase(get()) }
    factory { GetDailyUseCase(get()) }
}

private val mapModule = module {
    factory { ApiUiMapper() }
    factory { ApiDetailUiMapper() }
}

private val commonModule = module {
    single { SavedStateHandle() }
    single<ILocationManager> { ForecastLocationService(get()) }
    single<IRepository> { RepositoryImp(get(), get(), get()) }
    single<IRemoteData> { RemoteDataImp(get()) }
    single<Retrofit> {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        okHttpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter(KEY_NAME, API_KEY)
                .build()
            val requestBuilder = original.newBuilder()
                .url(url)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
        okHttpClientBuilder.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }
    single { BASE_URL }
}

val mModules = listOf(commonModule, viewModelModule, mapModule, useCaseModule)

