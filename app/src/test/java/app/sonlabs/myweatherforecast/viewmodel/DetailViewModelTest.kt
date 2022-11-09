package app.sonlabs.myweatherforecast.viewmodel

import android.content.Context
import app.cash.turbine.test
import app.sonlabs.myweatherforecast.data.Coord
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import app.sonlabs.myweatherforecast.di.mModules
import app.sonlabs.myweatherforecast.ui.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest : KoinTest {

    private val viewModel: DetailViewModel by inject()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            androidContext(Mockito.mock(Context::class.java))
            modules(mModules)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `get default UiResponse , then Idle is returned `() = runTest {
        viewModel.detail.test {
            assertTrue(awaitItem() is UiResponse.Idle)
        }
    }

    @Test
    fun `getDetail , then success is returned `() = runTest {
        val location = Coord(lat = 13.75, lon = 100.5167)
        viewModel.getDetail(location.lat, location.lon, true)
        viewModel.detail.test {
            if (awaitItem() is UiResponse.Success) {
                assertTrue(awaitItem() is UiResponse.Success<ForecastDetail>)
                val timezone = (awaitItem() as UiResponse.Success).data.timezone
                assertEquals(timezone, "Asia/Bangkok")
            }
        }
    }
}