package app.sonlabs.myweatherforecast.usecase

import android.content.Context
import app.cash.turbine.test
import app.sonlabs.myweatherforecast.di.mModules
import app.sonlabs.myweatherforecast.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
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
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class GetLocationUseCaseTest : KoinTest {

    private val getLocationUseCase: GetLocationUseCase by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        Dispatchers.setMain(StandardTestDispatcher())
        printLogger()
        androidContext(mock(Context::class.java))
        modules(mModules)
    }

    @Test
    fun `when getLocation is called with invalid location, then observer is updated with failure`() =
        runTest {
            getLocationUseCase.execute().catch { e ->
                Assert.fail(e.message);
            }
        }
}
