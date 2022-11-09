package app.sonlabs.myweatherforecast

import android.app.Application
import app.sonlabs.myweatherforecast.di.mModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class WeatherForecastApp : Application() {
    override fun onCreate() {
        super.onCreate()
         startKoin {
            androidContext(this@WeatherForecastApp)
            modules(mModules)
        }
    }
}
