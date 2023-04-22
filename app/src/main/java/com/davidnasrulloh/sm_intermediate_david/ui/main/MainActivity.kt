package com.davidnasrulloh.sm_intermediate_david.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidnasrulloh.sm_intermediate_david.R
import com.davidnasrulloh.sm_intermediate_david.adapter.StoryListAdapter
import com.davidnasrulloh.sm_intermediate_david.data.remote.response.Story
import com.davidnasrulloh.sm_intermediate_david.databinding.ActivityMainBinding
import com.davidnasrulloh.sm_intermediate_david.ui.auth.AuthActivity
import com.davidnasrulloh.sm_intermediate_david.ui.story.create.CreateStoryActivity
import com.davidnasrulloh.sm_intermediate_david.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        token = intent.getStringExtra(EXTRA_TOKEN)!!

        setSwipeRefreshLayout()
        setRecyclerView()
        getAllStories()

        binding.fabCreateStory.setOnClickListener{
            Intent(this, CreateStoryActivity::class.java).also {
                startActivity(it)
            }
        }
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

    private fun setRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(this)
        listAdapter = StoryListAdapter()

        recyclerView = binding.rvStories
        recyclerView.apply {
            adapter = listAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun updateRecyclerViewData(stories: List<Story>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        listAdapter.submitList(stories)
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun getAllStories(){
        binding.viewLoading.animateVisibility(true)
        binding.swipeRefresh.isRefreshing = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.getAllStories(token).collect{result ->
                    result.onSuccess { response ->
                        updateRecyclerViewData(response.stories)

                        binding.apply {
                            tvNotFoundStory.animateVisibility(response.stories.isEmpty())
                            ivNotFoundStory.animateVisibility(response.stories.isEmpty())
                            rvStories.animateVisibility(response.stories.isNotEmpty())
                            viewLoading.animateVisibility(false)
                            swipeRefresh.isRefreshing = false
                        }
                    }

                    result.onFailure {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.error_occurred_message),
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.apply {
                            tvNotFoundStory.animateVisibility(true)
                            ivNotFoundStory.animateVisibility(true)
                            rvStories.animateVisibility(false)
                            viewLoading.animateVisibility(false)
                            swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }
        }
    }

    private fun setSwipeRefreshLayout(){
        binding.swipeRefresh.setOnRefreshListener {
            getAllStories()
            binding.viewLoading.animateVisibility(false)
        }
    }
}