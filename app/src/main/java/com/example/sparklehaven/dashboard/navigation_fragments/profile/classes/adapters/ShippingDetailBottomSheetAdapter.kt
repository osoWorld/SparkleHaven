package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.ShippingDetailBottomSheetModel
import com.example.sparklehaven.databinding.ShippingDetailLayoutBinding
import com.example.sparklehaven.product.checkout.classes.model.Address

class ShippingDetailBottomSheetAdapter :
    RecyclerView.Adapter<ShippingDetailBottomSheetAdapter.ShippingDetailBottomSheetViewHolder>() {
    private var shippingDetailList: List<Address> = listOf()

    class ShippingDetailBottomSheetViewHolder(val binding: ShippingDetailLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingDetailBottomSheetViewHolder {
        return ShippingDetailBottomSheetViewHolder(
            ShippingDetailLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = shippingDetailList.size

    override fun onBindViewHolder(holder: ShippingDetailBottomSheetViewHolder, position: Int) {
        val shippingDetail = shippingDetailList[position]

        holder.binding.apply {
            shippingDetailUserName.text = shippingDetail.name
            shippingDetailPhoneNumber.text = shippingDetail.phone
            shippingDetailAddress.text = shippingDetail.address
            shippingDetailCity.text = shippingDetail.city
            shippingDetailPostalCode.text = shippingDetail.postalCode
            shippingDetailState.text = shippingDetail.state
            shippingDetailCountry.text = shippingDetail.country
        }
    }

    fun updateItems(newItems: List<Address>) {
        shippingDetailList = newItems
        notifyDataSetChanged()
    }
}