package com.mexator.petfinder_client.ui.fragment.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.utils.Optional
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.result_item.*
import java.net.URL

class PetHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    private var compositeDisposable = CompositeDisposable()
    private val LOADING_POSITION = 0
    private val PHOTO_POSITION = 1

    fun bind(pet: Pet) {
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()

        petDescription.text = pet.description
        petAge.text = pet.age
        petName.text = pet.name

        no_img.visibility = View.INVISIBLE
        photoWrapper.displayedChild = LOADING_POSITION
        setImage(pet)
    }

    private fun setImage(pet: Pet) {
        petPreview.setImageResource(R.drawable.photo_placeholder)
        val job = getPhoto(pet)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photo ->
                if (!photo.isEmpty()) {
                    petPreview.setImageBitmap(photo.get())
                } else
                    no_img.visibility = View.VISIBLE
                photoWrapper.displayedChild = PHOTO_POSITION
            }) {
                no_img.visibility = View.VISIBLE
                photoWrapper.displayedChild = PHOTO_POSITION
            }
        compositeDisposable.add(job)
    }

    private fun getPhoto(pet: Pet): Single<Optional<Bitmap>> {
        var mUrl: URL? = null

        pet.photos?.let {
            if (it.isNotEmpty()) {
                mUrl = URL(it[0].small)
            }
        }

        mUrl?.let {
            return Single
                .defer {
                    Single.just(
                        Optional(
                            BitmapFactory.decodeStream(it.openStream())
                        )
                    )
                }
                .subscribeOn(Schedulers.io())
        }

        return Single.just(Optional<Bitmap>(null))
    }
}