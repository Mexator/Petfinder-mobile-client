package com.mexator.petfinder_client.ui.fragment.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.RequestDisposable
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.pojo.Pet
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.result_item.*

class PetHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    private var disposable: RequestDisposable? = null
    private val LOADING_POSITION = 0
    private val PHOTO_POSITION = 1

    fun bind(pet: Pet) {
        disposable?.dispose()

        petDescription.text = pet.description
        petAge.text = pet.age
        petName.text = pet.name

        no_img.visibility = View.INVISIBLE
        photoWrapper.displayedChild = LOADING_POSITION

        if (pet.photos?.isEmpty() == true) {
            petPreview.setImageResource(R.drawable.photo_placeholder)
            no_img.visibility = View.VISIBLE
            photoWrapper.displayedChild = PHOTO_POSITION
        } else {
            disposable = petPreview.load(pet.photos!![0].small) {
                this.target {
                    petPreview.setImageDrawable(it)
                    photoWrapper.displayedChild = PHOTO_POSITION
                }
            }
        }
    }
}