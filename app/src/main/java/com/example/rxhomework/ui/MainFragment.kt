package com.example.rxhomework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxhomework.R
import com.example.rxhomework.mvvm.viewmodel.MainViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var viewModel: MainViewModel? = null
    private var petDataAdapter: PetDataAdapter? = null
    private var compositeDisposable = CompositeDisposable()

    fun getPets() {
        animal_type_spinner?.selectedItem.toString().let {
            viewModel?.updatePetsList(it, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        subscribeToProgressIndicator()

        petDataAdapter = PetDataAdapter()

        val job = viewModel!!.getPetsList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            if (!it.isNullOrEmpty()) {
                recyclerView.visibility = View.VISIBLE
                petDataAdapter?.setData(it)
            } else {
                recyclerView.visibility = View.GONE
                Toast.makeText(context, "No Records Found", Toast.LENGTH_LONG).show()
            }
        }
        compositeDisposable.add(job)

        val itemSelectorListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

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
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = petDataAdapter
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
}
