package com.mexator.petfinder_client.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.mvvm.viewmodel.MainActivityViewModel
import com.mexator.petfinder_client.mvvm.viewstate.MainActivityViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setupStateSubscription()

        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_logout -> logout()
                R.id.action_to_liked_pets -> goToLiked()
                R.id.action_search -> goToSearch()
                else -> {
                }
            }
            true
        }
        viewModel.fetchUser()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }

    private fun goToSearch() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_main)
    }

    private fun goToLiked() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_liked)
    }

    private fun logout() {
        viewModel.logout()
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        finish()
    }

    private fun setupStateSubscription() {
        val job = viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                applyState(it)
            }
        compositeDisposable.add(job)
    }

    private fun applyState(state: MainActivityViewState) {
        state.user?.let { user ->
            drawer_header_name.text =
                getString(R.string.username_placeholder, user.firstName, user.lastName)
            drawer_header_email.text = user.email
        }
    }
}
