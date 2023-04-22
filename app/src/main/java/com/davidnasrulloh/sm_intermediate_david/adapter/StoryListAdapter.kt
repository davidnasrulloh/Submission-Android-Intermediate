package com.davidnasrulloh.sm_intermediate_david.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidnasrulloh.sm_intermediate_david.data.remote.response.Story
import com.davidnasrulloh.sm_intermediate_david.databinding.LayoutStoryItemBinding
import com.davidnasrulloh.sm_intermediate_david.ui.story.detail.DetailStoryActivity
import com.davidnasrulloh.sm_intermediate_david.ui.story.detail.DetailStoryActivity.Companion.EXTRA_DETAIL
import com.davidnasrulloh.sm_intermediate_david.utils.setImageFromUrl
import com.davidnasrulloh.sm_intermediate_david.utils.setLocalDateFormat

class StoryListAdapter : ListAdapter<Story, StoryListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: LayoutStoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: Story) {
            binding.apply {
                tvStoryUsername.text = story.name
                tvStoryDescription.text = story.description
                ivStoryImage.setImageFromUrl(context, story.photoUrl)
                tvStoryDate.setLocalDateFormat(story.createdAt)

                // On item clicked
                root.setOnClickListener {
                    // Set ActivityOptionsCompat for SharedElement
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(ivStoryImage, "story_image"),
                            Pair(tvStoryUsername, "username"),
                            Pair(tvStoryDate, "date"),
                            Pair(tvStoryDescription, "description")
                        )

                    Intent(context, DetailStoryActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_DETAIL, story)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListAdapter.ViewHolder {
        val binding = LayoutStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryListAdapter.ViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(holder.itemView.context, story)
    }
}