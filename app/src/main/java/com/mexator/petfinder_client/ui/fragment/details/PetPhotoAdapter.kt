package com.mexator.petfinder_client.ui.fragment.details

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.pojo.PetPhoto
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detail_photo_page.*

class PetPhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer

val PetPhotoDiff = object : DiffUtil.ItemCallback<PetPhoto>() {
    override fun areItemsTheSame(oldItem: PetPhoto, newItem: PetPhoto): Boolean {
        return oldItem.small == newItem.small
    }

    override fun areContentsTheSame(oldItem: PetPhoto, newItem: PetPhoto): Boolean {
        return oldItem == newItem
    }
}

class PetPhotoAdapter : ListAdapter<PetPhoto, PetPhotoViewHolder>(PetPhotoDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPhotoViewHolder =
        PetPhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_photo_page, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: PetPhotoViewHolder, position: Int) {
        with(holder) {
            photo.setImageResource(R.drawable.photo_placeholder)
        }
    }
}