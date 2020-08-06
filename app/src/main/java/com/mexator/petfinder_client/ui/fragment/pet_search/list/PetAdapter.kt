package com.mexator.petfinder_client.ui.fragment.pet_search.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.extensions.getTag
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

        val job = petRepository.getPetPreview(pet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEvent { _, _ -> photoWrapper.displayedChild = PHOTO_POSITION }
            .subscribe(
                { value -> petPreview.setImageDrawable(value) },
                {
                    Log.d(getTag(), "Failed to load preview for pet with id=${pet.id}")
                    petPreview.setImageResource(R.drawable.photo_placeholder)
                },
                {
                    petPreview.setImageResource(R.drawable.photo_placeholder)
                    no_img.visibility = View.VISIBLE
                })

        compositeDisposable.add(job)
    }

    fun dispose() {
        compositeDisposable.clear()
    }
}

private object PetDiffCallback : DiffUtil.ItemCallback<PetModel>() {
    override fun areItemsTheSame(oldItem: PetModel, newItem: PetModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PetModel, newItem: PetModel): Boolean {
        return true
    }

}

class PetAdapter(private val onClickCallback: (PetModel) -> Unit) :
    ListAdapter<PetModel, PetHolder>(
        PetDiffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder =
        PetHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.result_item, parent, false)
        )

    override fun getItemViewType(position: Int): Int {
        return R.layout.result_item
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.bind(currentList[position])
        holder.containerView.setOnClickListener { onClickCallback(currentList[position]) }
    }

    override fun onViewRecycled(holder: PetHolder) {
        super.onViewRecycled(holder)
        holder.dispose()
    }
}