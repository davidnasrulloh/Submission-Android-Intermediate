package com.davidnasrulloh.sm_intermediate_david.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidnasrulloh.sm_intermediate_david.R

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}