package com.mexator.petfinder_client.ui.petlist

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

/**
 * [PetHolder] class represents [PetModel] with some additional parameters needed
 * to display it in RecyclerView with [PetAdapter]
 */
data class PetHolder(
    val pet: PetModel,
    val isFavorite: Boolean
)

/**
 * [PetViewHolder] is a class that inherits [RecyclerView.ViewHolder] and is capable of showing
 * [PetModel]
 */
class PetViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer, KoinComponent {
    private val petRepository: PetRepository by inject()
    private val LOADING_POSITION = 0
    private val PHOTO_POSITION = 1
    private val compositeDisposable = CompositeDisposable()

    /**
     * Setup child Views so that they will correctly show info about the [pet]
     */
    fun bind(petHolder: PetHolder, likeCallback: (PetModel, Boolean) -> Unit) {
        compositeDisposable.clear()

        val pet = petHolder.pet

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

        checkbox_like.setOnCheckedChangeListener(null)
        checkbox_like.isChecked = petHolder.isFavorite
        checkbox_like.setOnCheckedChangeListener { _, isChecked ->
            likeCallback(pet, isChecked)
        }
    }

    fun dispose() {
        compositeDisposable.clear()
    }
}

/**
 * The [PetHolderDiffCallback] is used to decide whether onBindViewHolder should be called
 * on this cell of [RecyclerView] or not
 */
private object PetHolderDiffCallback : DiffUtil.ItemCallback<PetHolder>() {
    override fun areItemsTheSame(oldItem: PetHolder, newItem: PetHolder): Boolean {
        return oldItem.pet.id == newItem.pet.id
    }

    override fun areContentsTheSame(oldItem: PetHolder, newItem: PetHolder): Boolean {
        return oldItem.isFavorite == newItem.isFavorite
    }
}

/**
 * [PetAdapter] is an adapter class to show list of [PetModel] in a [RecyclerView]
 */
class PetAdapter(
    private val onClickCallback: (PetModel) -> Unit,
    private val likeCallback: (PetModel, Boolean) -> Unit
) :
    ListAdapter<PetHolder, PetViewHolder>(PetHolderDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder =
        PetViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.result_item, parent, false)
        )

    override fun getItemViewType(position: Int): Int {
        return R.layout.result_item
    }

    override fun onBindViewHolder(viewHolder: PetViewHolder, position: Int) {
        val petHolder: PetHolder = currentList[position]
        viewHolder.bind(petHolder, likeCallback)
        viewHolder.containerView.setOnClickListener { onClickCallback(petHolder.pet) }
    }

    override fun onViewRecycled(viewHolder: PetViewHolder) {
        super.onViewRecycled(viewHolder)
        viewHolder.dispose()
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].pet.id
    }
}