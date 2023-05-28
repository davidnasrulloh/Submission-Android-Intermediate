package com.davidnasrulloh.sm_intermediate_david.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidnasrulloh.sm_intermediate_david.adapter.LoadingStateAdapter
import com.davidnasrulloh.sm_intermediate_david.adapter.StoryListAdapter
import com.davidnasrulloh.sm_intermediate_david.data.local.entity.Story
import com.davidnasrulloh.sm_intermediate_david.databinding.FragmentHomeBinding
import com.davidnasrulloh.sm_intermediate_david.ui.main.MainActivity
import com.davidnasrulloh.sm_intermediate_david.ui.story.create.CreateStoryActivity
import com.davidnasrulloh.sm_intermediate_david.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: StoryListAdapter

    private var token: String = ""
    private val homeViewModel: HomeViewModel by viewModels()

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = requireActivity().intent.getStringExtra(MainActivity.EXTRA_TOKEN) ?: ""

        setSwipeRefreshLayout()
        setRecyclerView()
        getAllStories()

        binding?.fabCreateStory?.setOnClickListener {
            Intent(requireContext(), CreateStoryActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }

    }

    // Get all stories data and set the related views state
    private fun getAllStories() {
        homeViewModel.getAllStories(token).observe(viewLifecycleOwner) { result ->
            updateRecyclerViewData(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // Set the RecyclerView UI state
    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        listAdapter = StoryListAdapter()
        binding?.viewLoading?.animateVisibility(true)
        binding?.swipeRefresh?.isRefreshing = true

        // Pager LoadState listener
        listAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && listAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                // List empty or error
                binding?.apply {
                    tvNotFoundStory.animateVisibility(true)
                    ivNotFoundStory.animateVisibility(true)
                    rvStories.animateVisibility(false)
                    viewLoading.animateVisibility(false)
                    swipeRefresh.isRefreshing = false
                }
            } else {
                // List not empty
                binding?.apply {
                    tvNotFoundStory.animateVisibility(false)
                    ivNotFoundStory.animateVisibility(false)
                    rvStories.animateVisibility(true)
                    viewLoading.animateVisibility(false)
                    swipeRefresh.isRefreshing = false
                }
            }

            // SwipeRefresh status based on LoadState
            binding?.swipeRefresh?.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            recyclerView = binding?.rvStories!!
            recyclerView.apply {
                adapter = listAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listAdapter.retry()
                    }
                )
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    // Set the SwipeRefreshLayout state
    private fun setSwipeRefreshLayout() {
        binding?.swipeRefresh?.setOnRefreshListener {
            getAllStories()
            binding?.viewLoading?.animateVisibility(false)
        }
    }

    // Update RecyclerView adapter data
    private fun updateRecyclerViewData(stories: PagingData<Story>) {
        // SaveInstanceState of recyclerview before add new data
        // It's prevent the recyclerview to scroll again to the top when load new data
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

        // Add data to the adapter
        listAdapter.submitData(lifecycle, stories)

        // Restore last recyclerview state
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}