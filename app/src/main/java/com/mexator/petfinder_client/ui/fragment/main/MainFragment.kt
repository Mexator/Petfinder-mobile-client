package com.mexator.petfinder_client.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.mvvm.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    private var compositeDisposable = CompositeDisposable()

    private val PRELOAD_MARGIN = 10
    private val petDataSource = emptyDataSourceTyped<Pet>()

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

        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Setup Subscriptions
        subscribeToViewState()
        setupRecyclerView()
        setupSpinner()
        setupSwipeRefresh()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(false)

        recyclerView.setup {
            withDataSource(petDataSource)
            withLayoutManager(LinearLayoutManager(context))
            withItem<Pet, PetHolder>(R.layout.result_item) {
                onBind(::PetHolder) { _, item ->
                    bind(item)
                }
            }
            withClickListener { pos ->
                val bundle = Bundle()
                bundle.putParcelable("content", petDataSource[pos])
                findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle) }
        }

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
            viewModel.updatePetsList(animal_type_spinner.selectedItem.toString(), null)
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
                if (!(first and viewModel.listNotEmpty))
                    viewModel.updatePetsList(animal_type_spinner.selectedItem.toString(), null)
                first = false
            }
        }
    }

    private fun subscribeToViewState() {
        val job = viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                Log.d((this as Any).getTag(), "State update: updating = ${state.updating}")
                petDataSource.set(state.petList, { old, new -> old.id == new.id }) { _, _ -> true }
                pets_loading.visibility =
                    if (state.updating) {
                        View.VISIBLE
                    } else
                        View.INVISIBLE
                if (!state.updating) swipeRefresh.isRefreshing = false
            }
        compositeDisposable.add(job)
    }
}
