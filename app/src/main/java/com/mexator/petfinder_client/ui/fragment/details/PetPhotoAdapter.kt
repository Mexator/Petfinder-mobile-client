package com.mexator.petfinder_client.ui.fragment.details

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.mvvm.viewmodel.pet_detail.PhotoWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detail_photo_page.*

class PetPhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer

private object PetPhotoDiff : DiffUtil.ItemCallback<PhotoWrapper>() {
    override fun areItemsTheSame(oldItem: PhotoWrapper, newItem: PhotoWrapper): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: PhotoWrapper, newItem: PhotoWrapper): Boolean {
        return newItem.photo == null
    }
}

class PetPhotoAdapter : ListAdapter<PhotoWrapper, PetPhotoViewHolder>(PetPhotoDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPhotoViewHolder =
        PetPhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_photo_page, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: PetPhotoViewHolder, position: Int) {
        holder.photo.setImageDrawable(currentList[position].photo)
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].id
    }
}