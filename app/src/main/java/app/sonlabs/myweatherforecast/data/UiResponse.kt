package app.sonlabs.myweatherforecast.data

sealed class UiResponse<out T> {
    data class Success<T>(val data: T) : UiResponse<T>()
    data class Error(val message: String?) : UiResponse<Nothing>()
    object Idle : UiResponse<Nothing>()
    object Loading : UiResponse<Nothing>()
}
