package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("flagTheme", flagTheme)
    }


}