package com.mexator.petfinder_client.ui.fragment.details

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
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
        if (oldItem.isPlaceholder and !newItem.isPlaceholder) return false
        return newItem.photo == null
    }
}

/**
 * Class that can be used as adapter for photos, and has a placeholder that is used when the
 * submitted list is empty
 */
class PetPhotoAdapter : ListAdapter<PhotoWrapper, PetPhotoViewHolder>(PetPhotoDiff) {
    public var placeholderPhoto: Drawable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPhotoViewHolder =
        PetPhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_photo_page, parent, false)
        )

    override fun submitList(list: List<PhotoWrapper>?) {
        if (!list.isNullOrEmpty())
            super.submitList(list)
        else {
            super.submitList(listOf(PhotoWrapper(placeholderPhoto, 0, true)))
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: PetPhotoViewHolder, position: Int) {
        with(currentList[position]) {
            holder.photo.setImageDrawable(photo)
            holder.no_images.visibility =
                if (isPlaceholder)
                    View.VISIBLE
                else
                    View.INVISIBLE
            if (photo != null) holder.progress.visibility = View.INVISIBLE
        }
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].id
    }
}