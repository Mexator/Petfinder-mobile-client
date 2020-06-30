package com.example.rxhomework.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.example.rxhomework.R
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.mvvm.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private var viewModel: MainViewModel? = null
    private var compositeDisposable = CompositeDisposable()
    private var petDataSource = emptyDataSourceTyped<Pet>()
    private val TAG = MainFragment::class.simpleName

    fun getPets() {
        animal_type_spinner?.selectedItem.toString().let {
            viewModel?.updatePetsList(it, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        subscribeToProgressIndicator()

        val job = viewModel
            ?.getPetsList()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                petDataSource.set(it)
                swipeRefresh.isRefreshing = false
                if (it.isNullOrEmpty()) {
                    Toast.makeText(context, "No Records Found", Toast.LENGTH_LONG).show()
                }
            },
                { Log.e(TAG, it.toString()) })


        job?.let { compositeDisposable.add(it) }

        val itemSelectorListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getPets()
            }
        }
        animal_type_spinner?.onItemSelectedListener = itemSelectorListener
        recyclerView.visibility = View.VISIBLE

        setupRecyclerView()
        setupSwipeRefresh()
    }

    // Uses progress boolean to show and hide progressBar when needed
    private fun subscribeToProgressIndicator() {
        val job =
            viewModel!!.getUpdating()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
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
            withDataSource(petDataSource)
            withLayoutManager(LinearLayoutManager(context))
            withItem<Pet, PetHolder>(R.layout.result_item) {
                onBind(::PetHolder) { _, item ->
                    bind(item)
                }
                hasStableIds { it.id }
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener { getPets() }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
