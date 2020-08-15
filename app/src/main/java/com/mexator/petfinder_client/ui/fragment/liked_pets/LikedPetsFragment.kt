package com.mexator.petfinder_client.ui.fragment.liked_pets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.mvvm.viewmodel.liked_pets.LikedPetViewModel
import com.mexator.petfinder_client.mvvm.viewstate.LikedPetsViewState
import com.mexator.petfinder_client.ui.fragment.pet_search.list.PetAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class LikedPetsFragment : Fragment() {
    private val viewModel: LikedPetViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()
    private val adapter: PetAdapter = PetAdapter { }

    init {
        adapter.setHasStableIds(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_liked, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewState()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun subscribeToViewState() {
        val job = viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> applyStateChange(state) }
        compositeDisposable.add(job)
    }

    private fun applyStateChange(state: LikedPetsViewState) {

    }
}