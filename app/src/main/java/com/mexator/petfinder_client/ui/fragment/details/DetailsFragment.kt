package com.mexator.petfinder_client.ui.fragment.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.DataSource
import com.mexator.petfinder_client.data.Repository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.extensions.getText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.ext.android.inject

class DetailsFragment : Fragment() {
    private lateinit var pet: PetModel
    private val repository: Repository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pet = it["content"] as PetModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhotos()
        setPetFields()
        setupButton()
    }

    private fun setupPhotos() {
        val adapter = PetPhotoAdapter()

        repository.getPetPhotos(pet, DataSource.PhotoSize.MEDIUM)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                adapter.submitList(value)
            }

        pager.adapter = adapter
        TabLayoutMediator(tabs, pager, true) { _, _ -> }.attach()
    }

    private fun setPetFields() {
        detail_age.text = pet.age
        detail_type.text = pet.type
        detail_name.text = pet.name
        detail_description.movementMethod = LinkMovementMethod.getInstance()

        val description = pet.description ?: ""
        val desc = context?.getText(R.string.link_read_more, pet.url, description)
        detail_description.text = desc

        detail_breed.text =
            when {
                pet.breeds.mixed && (pet.breeds.secondary != null) -> getString(
                    R.string.breed_placeholder,
                    pet.breeds.primary,
                    pet.breeds.secondary
                )
                pet.breeds.unknown -> getString(R.string.breed_unknown)
                else -> pet.breeds.primary
            }
        detail_gender.text = pet.gender
    }

    private fun setupButton() {
        button_ask_about.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(pet.url)
            startActivity(browserIntent)
        }
    }
}
