package com.mexator.petfinder_client.ui.fragment.pet_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.mvvm.viewmodel.PetSearchViewModel
import com.mexator.petfinder_client.ui.fragment.pet_search.list.PetAdapter
import com.mexator.petfinder_client.ui.fragment.pet_search.list.PetErrorAdapter
import com.mexator.petfinder_client.ui.fragment.pet_search.list.PetLoadingAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

class PetSearchFragment : Fragment() {
    private val viewModel: PetSearchViewModel by viewModels()
    private var compositeDisposable = CompositeDisposable()

    private val PRELOAD_MARGIN = 10

    private val dataAdapter =
        PetAdapter { item ->
            val bundle = Bundle()
            bundle.putParcelable("content", item)
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
        }
    private val loadingAdapter =
        PetLoadingAdapter()
    private val errorAdapter =
        PetErrorAdapter {
            viewModel.loadNextPage()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d((this as Any).getTag(), "onCreateView()")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d((this as Any).getTag(), "onViewCreated()")

        setupSpinner()
        setupRecyclerView()
        setupSwipeRefresh()

        subscribeToViewState()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val concatAdapter = ConcatAdapter(dataAdapter, loadingAdapter, errorAdapter)
        recyclerView.adapter = concatAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                val totalItems = layoutManager.itemCount
                if (totalItems - lastPosition <= PRELOAD_MARGIN) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun setupSpinner() {
        animal_type_spinner?.onItemSelectedListener = object : OnItemSelectedListener {
            var first: Boolean = true

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!first or viewModel.refreshNeeded)
                    refresh()
                first = false
            }
        }
    }

    private fun subscribeToViewState() {
        val job = viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                Log.d((this as Any).getTag(), "State update: updating = ${state.updating}")
                dataAdapter.submitList(state.petList)

                loadingAdapter.showed = state.updating
                errorAdapter.error = state.error
                if (!state.updating) swipeRefresh.isRefreshing = false
            }
        compositeDisposable.add(job)
    }

    private fun refresh() {
        viewModel.reloadPetsList(animal_type_spinner.selectedItem.toString(), null)
    }
}
