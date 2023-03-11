package com.example.note_app.adapters

import android.view.LayoutInflater
import android.view.View
import com.example.note_app.R
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.models.onboardingItem

class OnboardingItemAdapter(private  val onboardingItems: List<onboardingItem>) : RecyclerView.Adapter<OnboardingItemAdapter.OnboardingItemViewHolder>(){
    inner class OnboardingItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val imageOnboarding = view.findViewById<ImageView>(R.id.imageOnboarding);
        private val textTitle = view.findViewById<TextView>(R.id.textTitle);
        private val textDesc = view.findViewById<TextView>(R.id.textDesc);

        fun bind(onboardingItem: onboardingItem){
            imageOnboarding.setImageResource(onboardingItem.onboardingImage);
            textTitle.text = onboardingItem.title;
            textDesc.text = onboardingItem.desc;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.bind(onboardingItems[position]);
    }

    override fun getItemCount(): Int {
        return onboardingItems.size;
    }
}