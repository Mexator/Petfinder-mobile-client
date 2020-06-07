package com.example.rxhomework.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rxhomework.R
import com.example.rxhomework.data.pojo.Pet
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.result_item.view.*
import java.net.URL

class PetDataAdapter : RecyclerView.Adapter<PetDataAdapter.DataViewHolder>() {
    private var petList = mutableListOf<Pet>()

    fun setData(list: List<Pet>) {
        petList.clear()
        petList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetDataAdapter.DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.result_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: PetDataAdapter.DataViewHolder, position: Int) {
        holder.setData(petList[position])
    }

    inner class DataViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun setData(pet: Pet) {
            itemView.apply {
                nameTextView.text = pet.name
                ageTextView.text = pet.age
                descriptionTextView.text = pet.description
                pet.photos?.let {
                    if (it.isNotEmpty()) {
                        val url = URL(it[0].small)
                        Single
                            .defer { Single.just(BitmapFactory.decodeStream(url.openStream())) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                { bmp -> pet_preview.setImageBitmap(bmp) },
                                {}
                            )
                    }
                }
            }
        }
    }
}
