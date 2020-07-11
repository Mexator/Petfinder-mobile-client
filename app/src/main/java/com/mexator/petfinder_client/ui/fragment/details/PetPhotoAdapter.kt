package com.mexator.petfinder_client.ui.fragment.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detail_photo_page.*

class PetPhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer

class PetPhotoAdapter : RecyclerView.Adapter<PetPhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPhotoViewHolder =
        PetPhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_detail_photo_page, parent, false)
        )

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: PetPhotoViewHolder, position: Int) {
        with(holder) {
            photo.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}