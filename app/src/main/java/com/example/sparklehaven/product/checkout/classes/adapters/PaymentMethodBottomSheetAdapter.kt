package com.example.sparklehaven.product.checkout.classes.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.databinding.PaymentCardLayoutBinding
import com.example.sparklehaven.product.checkout.classes.model.PaymentMethodBottomSheetModel

class PaymentMethodBottomSheetAdapter : RecyclerView.Adapter<PaymentMethodBottomSheetAdapter.PaymentMethodBottomSheetViewHolder> () {

    private var paymentMethodList : List<PaymentMethodBottomSheetModel> = listOf()
    private var selectedPosition = -1

    class PaymentMethodBottomSheetViewHolder (val binding : PaymentCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentMethodBottomSheetViewHolder {
        return PaymentMethodBottomSheetViewHolder(
            PaymentCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = paymentMethodList.size

    override fun onBindViewHolder(holder: PaymentMethodBottomSheetViewHolder, position: Int) {
        val paymentMethod = paymentMethodList[position]

        holder.binding.apply {
            paymentMethodImage.setImageResource(paymentMethod.paymentMethodIcon)
            paymentMethodName.text = paymentMethod.paymentMethodName
            paymentMethodRadioButton.isChecked = position == selectedPosition

            // Update the CardView background color based on selection state
//            if (position == selectedPosition) {
//                paymentMethodCard.setCardBackgroundColor(
//                    ContextCompat.getColor(holder.itemView.context, R.color.Beige)
//                )
//            } else {
//                paymentMethodCard.setCardBackgroundColor(
//                    ContextCompat.getColor(holder.itemView.context, R.color.white)
//                )
//            }
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

    fun updateItems(newItems: List<PaymentMethodBottomSheetModel>) {
        paymentMethodList = newItems
        notifyDataSetChanged()
    }
}