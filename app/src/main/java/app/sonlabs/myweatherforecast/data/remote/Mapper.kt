package app.sonlabs.myweatherforecast.data.remote

interface Mapper<T : Any, V : Any> {
    fun map(data: T): V
}
