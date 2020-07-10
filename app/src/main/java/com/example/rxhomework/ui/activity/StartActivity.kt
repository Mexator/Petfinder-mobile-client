package com.example.rxhomework.ui.activity

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxhomework.Accounts.ACCOUNT_TYPE
import org.koin.core.KoinComponent
import org.koin.core.inject

class StartActivity : AppCompatActivity(), KoinComponent {
    private val accountManager: AccountManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navFlags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION

        if (isAccountExist()) {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = navFlags
            startActivity(mainIntent)
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.flags = navFlags
            startActivity(loginIntent)
        }
    }

    private fun isAccountExist(): Boolean =
        accountManager.getAccountsByType(ACCOUNT_TYPE).isNotEmpty()
}