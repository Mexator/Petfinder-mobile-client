package com.example.rxhomework.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rxhomework.R
import com.example.rxhomework.api_interaction.APIKeysHolder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Setup observer that will perform token update
        lifecycle.addObserver(APIKeysHolder)
    }
}
