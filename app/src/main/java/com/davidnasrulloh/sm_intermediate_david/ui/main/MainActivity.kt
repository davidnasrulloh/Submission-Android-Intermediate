package com.davidnasrulloh.sm_intermediate_david.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.davidnasrulloh.sm_intermediate_david.R
import com.davidnasrulloh.sm_intermediate_david.adapter.StoryListAdapter
import com.davidnasrulloh.sm_intermediate_david.data.remote.response.Story
import com.davidnasrulloh.sm_intermediate_david.databinding.ActivityMainBinding
import com.davidnasrulloh.sm_intermediate_david.ui.auth.AuthActivity
import com.davidnasrulloh.sm_intermediate_david.ui.story.create.CreateStoryActivity
import com.davidnasrulloh.sm_intermediate_david.utils.animateVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: StoryListAdapter

    private var token: String = ""
    private val viewModel: MainViewModel by viewModels()

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Bottom navigation
        val navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_location
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_logout -> {
                viewModel.saveAuthToken("")
                Intent(this, AuthActivity::class.java).also { intent ->
                    startActivity(intent)
                    finish()
                }
                Toast.makeText(this@MainActivity, "Logout success", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}