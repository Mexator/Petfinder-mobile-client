package com.example.rxhomework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rxhomework.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var viewModel: MainViewModel? = null
    private var petDataAdapter: PetDataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel?.petsList?.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                recyclerView.visibility = View.VISIBLE
                petDataAdapter?.setData(it)
            } else {
                recyclerView.visibility = View.GONE
                Toast.makeText(context, "No Records Found", Toast.LENGTH_LONG).show()
            }
        })
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
        animal_breed_spinner?.onItemSelectedListener = itemSelectorListener
        animal_type_spinner?.onItemSelectedListener = itemSelectorListener
    }

    fun getPets() {
        animal_breed_spinner?.selectedItem.toString().let {
            val type = animal_type_spinner?.selectedItem.toString()
            viewModel?.getPets(type, it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
