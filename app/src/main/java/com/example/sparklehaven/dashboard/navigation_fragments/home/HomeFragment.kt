package com.example.sparklehaven.dashboard.navigation_fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codebyashish.autoimageslider.Enums.ImageActionTypes
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Interfaces.ItemsListener
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sparklehaven.R
import com.example.sparklehaven.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ItemsListener {
    private lateinit var binding: FragmentHomeBinding
    private var listener : ItemsListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        // initialization of the listener
        listener = this

        // create an imageArrayList which extend ImageSlideModel class
        val autoImageList : ArrayList<ImageSlidesModel> = ArrayList()


        autoImageList.add(ImageSlidesModel(R.drawable.black_and_white_silver_earrings))
        autoImageList.add(ImageSlidesModel(R.drawable.black_diamond_ring_limited_tag))
        autoImageList.add(ImageSlidesModel(R.drawable.blue_gray_modern_rings))

        autoImageList.add(ImageSlidesModel(R.drawable.brown_and_white_simple_jewelry_dd))
        autoImageList.add(ImageSlidesModel(R.drawable.golden_wedding_rings))

        // set the added images inside the AutoImageSlider
        binding.autoImageSlider.setImageList(autoImageList, ImageScaleType.FIT)

        // set any default animation or custom animation (setSlideAnimation(ImageAnimationTypes.ZOOM_IN))
        binding.autoImageSlider.setDefaultAnimation()

        // handle click event on item click
        binding.autoImageSlider.onItemClickListener(listener)

        return binding.root
    }

    override fun onItemChanged(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onTouched(actionTypes: ImageActionTypes?, position: Int) {
        TODO("Not yet implemented")
    }

}