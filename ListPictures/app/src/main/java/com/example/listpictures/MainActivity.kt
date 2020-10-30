package com.example.listpictures

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_card.view.*
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL


class MainActivity : AppCompatActivity() {

    companion object {
        fun genURLs(count: Int): ArrayList<String> {
            val randomURLs = ArrayList<String>()
            for (j in 0..count-1) {
                val randomString = (1..6)
                    .map { i -> kotlin.random.Random.nextInt(0, source.length) }
                    .map(source::get)
                    .joinToString("")

                randomURLs.add(main_url + randomString)
            }
            return randomURLs
        }
        const val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val main_url = "https://robohash.org/"
        var arr: ArrayList<String> = genURLs(10)
//        val progress = IntArray(10) { i -> i * 10 }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBlock.visibility = ProgressBar.VISIBLE;
        progressText.text = "0%"
        ImageLoader().execute(*arr.toTypedArray())
    }

    inner class ImageLoader : AsyncTask<String, Int, ArrayList<Bitmap>>() {
        private val activityRef = WeakReference(MainActivity)
        override fun doInBackground(vararg URL: String?): ArrayList<Bitmap>? {
            var value = 0
            val bitmap = ArrayList<Bitmap>()
            URL.forEach {
                try {
                    val input: InputStream = URL(it).openStream()
                    bitmap.add(BitmapFactory.decodeStream(input))
                    value += 1
                    publishProgress(value)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return bitmap
        }


        @SuppressLint("SetTextI18n")
        override fun onProgressUpdate(vararg args: Int?) {
            args[0]?.let { progressBar.progress = it }
            args[0]?.let {
                val value = it.toFloat() / arr.size.toFloat() * 100
                progressText.text = "${value.toInt()}%" }
        }

        override fun onPostExecute(result: ArrayList<Bitmap>?) {
            val activity = activityRef.get()
            if (activity != null) {
                if (result != null) {
                    val pictures_1 = genArray(arr, result)
                    progressBlock.visibility = ProgressBar.INVISIBLE
                    val adapter = GroupAdapter<GroupieViewHolder>().apply {
                        addAll(pictures_1)
                    }
                    pictureRV.adapter = adapter
                }
            }
        }

    }


    fun nextActivity(view: View) {
        val bitmap = (view.img.drawable as BitmapDrawable).bitmap
        val fullScreenIntent = Intent(this, fullScreenImageView::class.java).apply {
            putExtra("BitmapImage", bitmap)
        }
        startActivity(fullScreenIntent)
    }

    fun genArray(urls: ArrayList<String>, img_array: ArrayList<Bitmap>): MutableList<MainCardContainer> {
        return MutableList(urls.size) {
            MainCardContainer(Picture(urls[it], img_array[it]))
        }
    }

        

}