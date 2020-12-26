package com.example.weather

import io.reactivex.Single
import retrofit2.http.GET

interface OpenWeatherMapServiceRX {
    @GET("onecall?lat=59.937500&lon=30.308611&exclude=minutely,hourly,alerts&units=metric&appid=508af3c89d5fdcb4b9f030cea26e964e")
    fun listWeather(): Single<WeatherResponse>
}
