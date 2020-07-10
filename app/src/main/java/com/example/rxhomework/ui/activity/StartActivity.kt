package com.example.rxhomework.ui.activity

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import com.example.rxhomework.Accounts.ACCOUNT_TYPE
import org.koin.core.KoinComponent
import org.koin.core.inject

class StartActivity : AppCompatActivity(), KoinComponent {
    private val accountManager: AccountManager by inject()
    private val activityNavigator = ActivityNavigator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginDestination = activityNavigator.createDestination()
            .setIntent(Intent(this, LoginActivity::class.java))
        val mainDestination = activityNavigator.createDestination()
            .setIntent(Intent(this, MainActivity::class.java))

        if (isAccountExist()) {
            activityNavigator.navigate(mainDestination, null, null, null)
        } else {
            activityNavigator.navigate(loginDestination, null, null, null)
        }
    }

    private fun isAccountExist(): Boolean =
        accountManager.getAccountsByType(ACCOUNT_TYPE).isNotEmpty()
}