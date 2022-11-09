package app.sonlabs.myweatherforecast.usecase

import android.content.Context
import app.sonlabs.myweatherforecast.data.Coord
import app.sonlabs.myweatherforecast.di.mModules
import app.sonlabs.myweatherforecast.domain.SearchCityByLocationParams
import app.sonlabs.myweatherforecast.domain.SearchCityParams
import app.sonlabs.myweatherforecast.domain.usecase.SearchCityByLocationUseCase
import app.sonlabs.myweatherforecast.domain.usecase.SearchCityUseCase
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
class SearchCityByLocationUseCaseTest : KoinTest {

    private val searchCityByLocationUseCase: SearchCityByLocationUseCase by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        Dispatchers.setMain(StandardTestDispatcher())
        printLogger()
        androidContext(Mockito.mock(Context::class.java))
        modules(mModules)
    }

    @Test
    fun `when SearchCity is called with the location , then observer is updated with success`() =
        runTest {
            val param = SearchCityByLocationParams(
                location = Coord(lat = 13.75, lon = 100.5167), units = Constants.UNITS_METRIC
            )
            searchCityByLocationUseCase.execute(param).collectLatest {
                Assert.assertEquals(it.sys.country, "TH")
            }
        }
}
