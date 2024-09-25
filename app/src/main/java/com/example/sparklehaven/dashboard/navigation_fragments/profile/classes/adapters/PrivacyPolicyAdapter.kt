package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.FAQItem
import com.example.sparklehaven.databinding.ItemPrivacyPolicyBinding

class PrivacyPolicyAdapter(private val faqList: List<FAQItem>) : RecyclerView.Adapter<PrivacyPolicyAdapter.PrivacyPolicyViewHolder> () {

    inner class PrivacyPolicyViewHolder(val binding: ItemPrivacyPolicyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivacyPolicyViewHolder {
        return PrivacyPolicyViewHolder(
            ItemPrivacyPolicyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = faqList.size

    override fun onBindViewHolder(holder: PrivacyPolicyViewHolder, position: Int) {
        val privacyPolicyItem = faqList[position]
        holder.binding.apply {
            questionTextView.text = privacyPolicyItem.question
            answerTextView.text = privacyPolicyItem.answer

            if (privacyPolicyItem.isExpanded) {
                mainLinearLayout.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.gold_outline)
                innerLinearLayout.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.invisible_circular_background)
                imageIcon.setImageResource(R.drawable.minus32)
                questionTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.Gold))
                answerTextView.visibility = View.VISIBLE
                imageIcon.imageTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.Gold)
            } else {
                mainLinearLayout.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.invisible_circular_background)
                innerLinearLayout.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.gold_bg)
                imageIcon.setImageResource(R.drawable.plus32)
//                questionTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.darkThemeBg))
                answerTextView.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                privacyPolicyItem.isExpanded = !privacyPolicyItem.isExpanded
                notifyItemChanged(position)
            }
        }
    }
}