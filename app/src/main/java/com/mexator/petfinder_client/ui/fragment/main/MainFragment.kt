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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d((this as Any).getTag(), "onCreate()")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d((this as Any).getTag(), "onViewCreated()")

        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Setup Subscriptions
        setupRecyclerView()
        setupSpinner()
        setupSwipeRefresh()
        subscribeToProgressIndicator()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    // Uses progress boolean to show and hide progressBar when needed
    private fun subscribeToProgressIndicator() {
        val job =
            viewModel
                .getUpdatingStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d((this as Any).getTag(), "visible = $it")
                    pets_loading.visibility =
                        if (it) {
                            View.VISIBLE
                        } else
                            View.INVISIBLE
                }
        compositeDisposable.add(job)
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(false)

        recyclerView.setup {
            withDataSource(viewModel.petDataSource)
            withLayoutManager(LinearLayoutManager(context))
            withItem<Pet, PetHolder>(R.layout.result_item) {
                onBind(::PetHolder) { _, item ->
                    bind(item)
                }
            }
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
            Log.d((this as Any).getTag(), "swipeRefresh()")

            animal_type_spinner?.selectedItem.toString().let {
                viewModel.updatePetsList(it, null)
            }
        }
        val job = viewModel
            .getUpdatingStatus()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!it)
                    swipeRefresh.isRefreshing = it
            }
        compositeDisposable.add(job)
    }

    private fun setupSpinner() {
        animal_type_spinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                animal_type_spinner?.selectedItem.toString().let {
                    viewModel.updatePetsList(it, null)
                }
            }
        }
    }
}