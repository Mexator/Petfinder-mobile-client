package com.mexator.petfinder_client.ui.fragment.details

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detail_photo_page.*

class PetPhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer

val PetPhotoDiff = object : DiffUtil.ItemCallback<Drawable>() {
    override fun areItemsTheSame(oldItem: Drawable, newItem: Drawable): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Drawable, newItem: Drawable): Boolean {
        return false
    }
}

class PetPhotoAdapter : ListAdapter<Drawable, PetPhotoViewHolder>(PetPhotoDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPhotoViewHolder =
        PetPhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_photo_page, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: PetPhotoViewHolder, position: Int) {
        holder.photo.setImageDrawable(currentList[position])
    }
}