package com.davidnasrulloh.sm_intermediate_david.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidnasrulloh.sm_intermediate_david.R
import com.davidnasrulloh.sm_intermediate_david.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}