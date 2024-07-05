package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.ShippingDetailBottomSheetModel
import com.example.sparklehaven.databinding.ShippingDetailLayoutBinding

class ShippingDetailBottomSheetAdapter :
    RecyclerView.Adapter<ShippingDetailBottomSheetAdapter.ShippingDetailBottomSheetViewHolder>() {
    private var shippingDetailList: List<ShippingDetailBottomSheetModel> = listOf()
    private var selectedPosition = -1

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
            shippingDetailUserName.text = shippingDetail.shippingDetailUserName
            shippingDetailPhoneNumber.text = shippingDetail.shippingDetailPhoneNumber
            shippingDetailUserName.text = shippingDetail.shippingDetailUserName
            shippingDetailUserName.text = shippingDetail.shippingDetailUserName
            shippingDetailRadioButton.isChecked = position == selectedPosition

            // Update the CardView background color based on selection state
            if (position == selectedPosition) {
                shippingDetailCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.Beige)
                )
            } else {
                shippingDetailCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.white)
                )
            }
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                // Deselect the previously selected item
                notifyItemChanged(selectedPosition)

                // Select the current item
                selectedPosition = position
                notifyItemChanged(selectedPosition)
            }
        }
    }

    fun updateItems(newItems: List<ShippingDetailBottomSheetModel>) {
        shippingDetailList = newItems
        notifyDataSetChanged()
    }
}
