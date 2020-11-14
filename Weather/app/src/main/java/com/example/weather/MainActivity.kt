package com.example.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.lang.Math.round
import kotlin.math.roundToInt


class WeatherResponse(
    @field:Json(name = "timezone") var timezone: String,
    @field:Json(name = "current") var current: Current,
    @field:Json(name = "daily") var daily: List<Dialy>,
)

class Current(
    @field:Json(name = "humidity") var humidity: Int,
    @field:Json(name = "temp") var temp: Float,
    @field:Json(name = "clouds") var clouds: Int,
    @field:Json(name = "wind_speed") var wind_speed: Int,
    @field:Json(name = "weather") var weather: List<WeatherDescription>,
)

class Dialy(
    @field:Json(name = "temp") var temp: Temp,
    @field:Json(name = "weather") var weather: List<WeatherDescription>,
)

class Temp(
    @field:Json(name = "day") var day: Float,
)

class WeatherDescription(
    @field:Json(name = "main") var main: String,
    @field:Json(name = "icon") var icon: String,
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
        @GET("onecall?lat=59.937500&lon=30.308611&exclude=minutely,hourly,alerts&units=metric&appid=508af3c89d5fdcb4b9f030cea26e964e")
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
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder = "lat: " +
                            weatherResponse.current.temp.toString() +
                            "\n" +
//                            "timezone" +
//                            weatherResponse.weather[0].main

                    Log.d("MSG-ff", "${weatherResponse.daily[0].temp}")
                    val t = weatherResponse.current.temp.roundToInt()
                    if (t > 0) {
                        textView.text = "${t}°C"
                    } else {
                        textView.text = "-${t}°C"
                    }
                    textView2.text = weatherResponse.timezone + "\n" + weatherResponse.current.weather[0].main
                    textViewWindFLow.text = weatherResponse.current.wind_speed.toString()
                    textViewCloudy.text = weatherResponse.current.clouds.toString()
                    textViewHumidity.text = weatherResponse.current.humidity.toString()
                    val icon1 = weatherResponse.current.weather[0].icon
                    Picasso.get()
                        .load("https://openweathermap.org/img/wn/$icon1@2x.png")
                        .fit().centerCrop()
                        .into(icon)
                    fun genPredictionOnEveryDay(days: List<Dialy>) {
                        val imgDay = listOf(imgPN, imgVT, imgSR, imgCT, imgPT, imgSB, imgVS)
                        val textViewTempDay = listOf(textViewTempPN, textViewTempVT, textViewTempSR, textViewTempCT, textViewTempPT, textViewTempSB, textViewTempVS)
                        days.forEachIndexed { index, day ->
                            Log.d("MSGHELLO", "HELLO")
                            if (index != 0) {
                                val tempNow = day.temp.day.roundToInt()
                                if (tempNow >= 0) {
                                    textViewTempDay[index - 1].text = "+$tempNow"
                                } else {
                                    textViewTempDay[index - 1].text = "$tempNow"
                                }
                                val iconPath = day.weather[0].icon
                                Picasso.get()
                                    .load("https://openweathermap.org/img/wn/$iconPath@2x.png")
                                    .fit().centerCrop()
                                    .into(imgDay[index - 1])
                            }
                        }

                    }
                    genPredictionOnEveryDay(weatherResponse.daily)
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