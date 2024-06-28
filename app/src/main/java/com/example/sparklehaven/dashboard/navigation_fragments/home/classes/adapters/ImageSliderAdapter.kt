package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.sparklehaven.R

class ImageSliderAdapter(private val imageList: ArrayList<Int>, private val viewPager2: ViewPager2)
    : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sliderImageView: ImageView = itemView.findViewById(R.id.sliderImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_container, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE // This ensures infinite looping
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val actualPosition = position % imageList.size
        holder.sliderImageView.setImageResource(imageList[actualPosition])
    }
}
