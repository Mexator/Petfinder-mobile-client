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
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.extensions.getText
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {
    private lateinit var pet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pet = it["content"] as Pet
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
        adapter.submitList(pet.photos)
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
