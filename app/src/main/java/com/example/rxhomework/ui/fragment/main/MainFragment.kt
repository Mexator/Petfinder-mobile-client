package com.example.rxhomework.ui.fragment.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.example.rxhomework.R
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.mvvm.viewmodel.MainViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.result_item.*
import java.net.URL

class PetHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer

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
                    petDescription.text = item.description
                    petName.text = item.name
                    petAge.text = item.age
                    // TODO: Reusable loader
                    item.photos?.let {
                        if (it.isNotEmpty()) {
                            val url = URL(it[0].small)
                            Single
                                .defer { Single.just(BitmapFactory.decodeStream(url.openStream())) }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    { bmp -> petPreview.setImageBitmap(bmp) },
                                    {}
                                )

                        }
                    }
                }
                hasStableIds { it.id }
            }
        }
    }
}
