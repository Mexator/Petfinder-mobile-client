package com.mexator.petfinder_client.ui.fragment.pet_search.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R

class PetLoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class PetLoadingAdapter : RecyclerView.Adapter<PetLoadingHolder>() {
    var showed: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetLoadingHolder =
        PetLoadingHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.result_item_loading, parent, false)
        )

    override fun onBindViewHolder(holder: PetLoadingHolder, position: Int) {}

    override fun getItemViewType(position: Int): Int {
        return R.layout.result_item_loading
    }

    override fun getItemCount(): Int {
        return if (showed) 1 else 0
    }
}