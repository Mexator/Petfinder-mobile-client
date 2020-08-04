package com.mexator.petfinder_client.ui.fragment.pet_search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.model.PetModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.result_item.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PetHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer, KoinComponent {
    private val petRepository: PetRepository by inject()
    private val LOADING_POSITION = 0
    private val PHOTO_POSITION = 1
    private val compositeDisposable = CompositeDisposable()

    fun bind(pet: PetModel) {
        compositeDisposable.clear()

        petDescription.text = pet.description
        petAge.text = pet.age
        petName.text = pet.name

        no_img.visibility = View.INVISIBLE
        photoWrapper.displayedChild = LOADING_POSITION

        petPreview.setImageResource(R.drawable.photo_placeholder)
        no_img.visibility = View.VISIBLE

        val job = petRepository.getPetPhotos(pet, PetDataSource.PhotoSize.SMALL)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list, error ->
                if (error == null && !list.isNullOrEmpty()) {
                    petPreview.setImageDrawable(list[0])
                }
                photoWrapper.displayedChild = PHOTO_POSITION
            }
        compositeDisposable.add(job)
    }

    fun dispose() {
        compositeDisposable.clear()
    }
}