package com.example.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.math.roundToInt


@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class MainActivity : AppCompatActivity() {
    companion object{
        const val URL = "https://api.openweathermap.org/data/2.5/"
    }

    private var flagTheme = false
    private val retrofit =
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    private val service: OpenWeatherMapServiceRX = retrofit.create(OpenWeatherMapServiceRX::class.java)
    private var rxResponse: Single<WeatherResponse>? = service.listWeather()


    @SuppressLint("CommitPrefEdits")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { flagTheme = it.getBoolean("flagTheme") }
        setTheme(if (flagTheme) R.style.AppThemeLight else R.style.AppThemeDark)
        setContentView(R.layout.activity_main)
        switch1.setOnClickListener {
            flagTheme = !flagTheme
            recreate()
        }
        val prefs = getSharedPreferences("storage", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        fun setViewInfo() {
            textView.text = prefs.getString("textView", "")!!
            textView2.text = prefs.getString("textView2", "")!!
            textViewWindFLow.text = prefs.getString("textViewWindFLow", "")!!
            textViewCloudy.text = prefs.getString("textViewCloudy", "")!!
            textViewHumidity.text = prefs.getString("textViewHumidity", "")!!
            textViewTempPN.text = prefs.getString("textViewTempPN", "")!!
            textViewTempVT.text = prefs.getString("textViewTempVT", "")!!
            textViewTempSR.text = prefs.getString("textViewTempSR", "")!!
            textViewTempCT.text = prefs.getString("textViewTempCT", "")!!
            textViewTempPT.text = prefs.getString("textViewTempPT", "")!!
            textViewTempSB.text = prefs.getString("textViewTempSB", "")!!
            textViewTempVS.text = prefs.getString("textViewTempVS", "")!!
        }

        rxResponse
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : DisposableSingleObserver<WeatherResponse>() {
                override fun onSuccess(weatherResponse: WeatherResponse) {
//                    TODO("Not yet implemented")
                    Log.d("SUCCESS", "${weatherResponse.current.weather}")
                    editor.putInt("textView", weatherResponse.current.temp.roundToInt()).apply()
                    val t = weatherResponse.current.temp.roundToInt()
                    if (t > 0) {
                        editor.putString("textView", "+${t}°C").apply()
                    } else {
                        editor.putString("textView", "${t}°C").apply()
                    }
                    editor.putString(
                        "textView2",
                        weatherResponse.timezone + "\n" + weatherResponse.current.weather[0].main
                    ).apply()
                    editor.putString(
                        "textViewWindFLow",
                        weatherResponse.current.wind_speed.toString()
                    ).apply()
                    editor.putString("textViewCloudy", weatherResponse.current.clouds.toString())
                        .apply()
                    editor.putString(
                        "textViewHumidity",
                        weatherResponse.current.humidity.toString()
                    ).apply()
                    val icon1 = weatherResponse.current.weather[0].icon
                    Picasso.get()
                        .load("https://openweathermap.org/img/wn/$icon1@2x.png")
                        .fit().centerCrop()
                        .into(icon)
                    fun genSaveEveryDay(days: List<Daily>) {
                        val textViewTempDay = listOf(
                            "textViewTempPN",
                            "textViewTempVT",
                            "textViewTempSR",
                            "textViewTempCT",
                            "textViewTempPT",
                            "textViewTempSB",
                            "textViewTempVS"
                        )
                        days.forEachIndexed { index, day ->
                            if (index != 0) {
                                val tempNow = day.temp.day.roundToInt()
                                if (tempNow >= 0) {
                                    editor.putString(textViewTempDay[index - 1], "+$tempNow")
                                        .apply()
                                } else {
                                    editor.putString(textViewTempDay[index - 1], "$tempNow").apply()
                                }
                            }
                        }
                    }
                    genSaveEveryDay(weatherResponse.daily)
                    setViewInfo()
                }


                override fun onError(e: Throwable) {
                    setViewInfo()
                    Log.d("Error!!!", "onError $e")
                }
            })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("flagTheme", flagTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        rxResponse = null
    }

}
