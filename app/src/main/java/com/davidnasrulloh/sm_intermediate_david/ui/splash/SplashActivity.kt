package com.davidnasrulloh.sm_intermediate_david.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.davidnasrulloh.sm_intermediate_david.R
import com.davidnasrulloh.sm_intermediate_david.ui.auth.AuthActivity
import com.davidnasrulloh.sm_intermediate_david.ui.main.MainActivity
import com.davidnasrulloh.sm_intermediate_david.ui.main.MainActivity.Companion.EXTRA_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        determineUserDirection()
    }

    private fun determineUserDirection() {
        lifecycleScope.launch {
            viewModel.getAuthToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    Intent(this@SplashActivity, AuthActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
//                        Toast.makeText(
//                            this@SplashActivity,
//                            "Tidak ada token",
//                            Toast.LENGTH_SHORT
//                        ).show()
                } else {
                    Intent(this@SplashActivity, MainActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_TOKEN, token)
                        startActivity(intent)
                        finish()
                    }

//                        Toast.makeText(
//                            this@SplashActivity,
//                            "Tidak ada token",
//                            Toast.LENGTH_SHORT
//                        ).show()
                }
            }

        }
    }
}