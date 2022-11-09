package app.sonlabs.myweatherforecast.usecase

import android.content.Context
import app.sonlabs.myweatherforecast.di.mModules
import app.sonlabs.myweatherforecast.domain.GetDailyParams
import app.sonlabs.myweatherforecast.domain.usecase.GetDailyUseCase
import app.sonlabs.myweatherforecast.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class GetDailyUseCaseTest : KoinTest {

    private val getDailyUseCase: GetDailyUseCase by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        Dispatchers.setMain(StandardTestDispatcher())
        printLogger()
        androidContext(Mockito.mock(Context::class.java))
        modules(mModules)
    }

    @Test
    fun `when SearchCity is called with the city, then observer is updated with success`() =
        runTest {
            val param = GetDailyParams(
                lat = 13.75, lon = 100.5167, exclude = "daily", units = Constants.UNITS_METRIC
            )
            getDailyUseCase.execute(param).collectLatest {
                Assert.assertEquals(it.timezone, "Asia/Bangkok")
            }
        }
}
