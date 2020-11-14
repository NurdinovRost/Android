package com.example.weather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


class WeatherResponse(
    @field:Json(name = "lat") var lat: Float,
    @field:Json(name = "lon") var lon: Float,
    @field:Json(name = "timezone") var timezone: String,
    @field:Json(name = "current") var current: Current,
)

class Current(
    @field:Json(name = "humidity") var humidity: Int,
    @field:Json(name = "temp") var temp: Float,
    @field:Json(name = "clouds") var clouds: Int,
    @field:Json(name = "wind_speed") var wind_speed: Int,
)



@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class MainActivity : AppCompatActivity() {
    companion object{
        const val URL = "https://api.openweathermap.org/data/2.5/"
        const val lat = "59.937500"
        const val lon = "30.308611"
        const val exclude = "minutely,hourly,daily,alerts"
        const val API_KEY = "508af3c89d5fdcb4b9f030cea26e964e"
    }

    public interface OpenWeatherMapService {
        @GET("onecall?lat=59.937500&lon=30.308611&exclude=minutely,hourly,daily,alerts&units=imperial&appid=508af3c89d5fdcb4b9f030cea26e964e")
        fun listWeather(): Call<WeatherResponse>
    }

    private var flagTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { flagTheme = it.getBoolean("flagTheme") }
        setTheme(if (flagTheme) R.style.AppThemeLight else R.style.AppThemeDark)
        setContentView(R.layout.activity_main)
        switch1.setOnClickListener {
            flagTheme = !flagTheme
            recreate()
        }

        val retrofit =
            Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        val service = retrofit.create(OpenWeatherMapService::class.java)
        val call: Call<WeatherResponse> = service.listWeather()
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder = "lat: " +
                            weatherResponse.lat.toString() +
                            "\n" +
                            "temp: " +
                            weatherResponse.current.temp.toString() +
                            "\n" +
                            "timezone" +
                            weatherResponse.timezone

                    Log.d("MSG-ff", stringBuilder)
                    textView.text = weatherResponse.current.temp.toString()
                    textView2.text = weatherResponse.timezone
                    textViewWindFLow.text = weatherResponse.current.wind_speed.toString()
                    textViewCloudy.text = weatherResponse.current.clouds.toString()
                    textViewHumidity.text = weatherResponse.current.humidity.toString()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("MSG-ff", "FAIL GEGE")
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("flagTheme", flagTheme)
    }


}