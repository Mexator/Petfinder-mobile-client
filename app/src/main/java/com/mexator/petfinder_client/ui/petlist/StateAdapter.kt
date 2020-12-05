package com.mexator.petfinder_client.ui.petlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mexator.petfinder_client.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.result_error.*

/**
 * [PetLoadingAdapter] is used to display "Loading..." cell in Recycler view when it is needed
 * @property showed if false, the cell is invisible, and visible if it is true.
 */
class PetLoadingAdapter : RecyclerView.Adapter<PetLoadingAdapter.PetLoadingHolder>() {
    class PetLoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var showed: Boolean = false
        set(value) {
            // Show animation only if state changed
            if (field == value)
                return

            field = value
            if (value) {
                notifyItemInserted(0)
            } else notifyItemRemoved(0)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetLoadingHolder =
        PetLoadingHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.result_item_loading, parent, false)
        )

    override fun onBindViewHolder(holder: PetLoadingHolder, position: Int) {}

    override fun getItemViewType(position: Int): Int {
        return R.layout.result_item_loading
    }

    override fun getItemCount(): Int {
        return if (showed) 1 else 0
    }
}

class PetErrorHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(retryCallback: () -> Unit, errorText: String) {
        error_content.text = errorText
        button_retry.setOnClickListener { retryCallback() }
    }
}

/**
 * [PetErrorAdapter] is used to display "Loading..." cell in Recycler view when it is needed
 * @property error if null, the cell is invisible, and visible otherwise.
 */
class PetErrorAdapter(private val retryCallback: () -> Unit) :
    RecyclerView.Adapter<PetErrorHolder>() {
    var error: String? = null
        set(value) {
            // Show animation only if state changed
            if (field == value)
                return

            field = value
            if (value != null)
                notifyItemInserted(0)
            else
                notifyItemRemoved(0)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetErrorHolder =
        PetErrorHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.result_error, parent, false)
        )

    override fun getItemCount(): Int {
        return if (error != null) 1 else 0
    }

    override fun onBindViewHolder(holder: PetErrorHolder, position: Int) {
        error?.let {
            holder.bind(retryCallback, it)
        }
    }
}