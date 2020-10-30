package com.example.listpictures

import android.graphics.Bitmap
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_card.view.*


data class Picture(val title: String, val bm: Bitmap)

class MainCardContainer(
    val picture: Picture
) : Item() {

    override fun getLayout() = R.layout.item_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.title_text_view.text = picture.title
        viewHolder.itemView.img.setImageBitmap(picture.bm)
    }
}