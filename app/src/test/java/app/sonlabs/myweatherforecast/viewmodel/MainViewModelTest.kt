package app.sonlabs.myweatherforecast.viewmodel

import android.content.Context
import app.cash.turbine.test
import app.cash.turbine.testIn
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.di.mModules
import app.sonlabs.myweatherforecast.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
class MainViewModelTest : KoinTest {

    private val viewModel: MainViewModel by inject()

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
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun `get default units , then true is returned `() = runTest {
        viewModel.units.test {
            assertTrue(awaitItem())
        }
    }

    @Test
    fun `get default UiResponse , then Idle is returned `() = runTest {
        viewModel.forecast.test {
            assertTrue(awaitItem() is UiResponse.Idle)
        }
    }

    @Test
    fun `set units , then true is returned `() = runTest {
        viewModel.setUnits().invokeOnCompletion {
            assertFalse(viewModel.units.value)
        }
    }

    @Test
    fun `search city , then success is returned `() = runTest {
        viewModel.search("london")
        viewModel.forecast.test {
            if (awaitItem() is UiResponse.Success) {
                assertEquals((awaitItem() as UiResponse.Success).data.name.lowercase(), "london")
            }
        }
    }

    @Test
    fun `search loading, then fail is returned `() = runTest {
        viewModel.search("")
        viewModel.forecast.test {
            if (awaitItem() is UiResponse.Loading) {
                assertTrue(awaitItem() is UiResponse.Loading)
            }
        }
    }

    @Test
    fun `getData , then null is returned `() = runTest {
        val data = viewModel.getData()
        assertTrue(data == null)
    }

    @Test
    fun `getData , then non-null is returned `() = runTest {
        viewModel.search("london")
            .invokeOnCompletion {
                assertTrue(viewModel.getData() != null)
            }
    }

    @Test
    fun `getLocation , then error is returned `() = runTest {
        viewModel.getLocation()
        val result = viewModel.forecast.testIn(backgroundScope)
        if (result.awaitItem() is UiResponse.Error) {
            assertTrue(result.awaitItem() is UiResponse.Error)
        }
    }
}