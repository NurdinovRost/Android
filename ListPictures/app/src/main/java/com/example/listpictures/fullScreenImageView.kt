package com.example.listpictures

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_full_screen_image_view.*


class fullScreenImageView : AppCompatActivity() {

    companion object {
        const val IMAGE_ID = "image_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image_view)
        fullScreenImageShow()
    }

    fun fullScreenImageShow () {
        val intent = intent
        val bitmap = intent.getParcelableExtra<Parcelable>("BitmapImage") as Bitmap?
        fullScreenImage.setImageBitmap(bitmap)
    }

    fun moveToMainActivity(view: View) {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}