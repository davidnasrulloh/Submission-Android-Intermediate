package com.davidnasrulloh.sm_intermediate_david.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.davidnasrulloh.sm_intermediate_david.data.local.entity.Story
import com.davidnasrulloh.sm_intermediate_david.databinding.LayoutStoryItemBinding
import com.davidnasrulloh.sm_intermediate_david.ui.story.detail.DetailStoryActivity
import com.davidnasrulloh.sm_intermediate_david.ui.story.detail.DetailStoryActivity.Companion.EXTRA_DETAIL
import com.davidnasrulloh.sm_intermediate_david.utils.setImageFromUrl
import com.davidnasrulloh.sm_intermediate_david.utils.setLocalDateFormat

class StoryListAdapter : PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(holder.itemView.context, it) }
    }

    class ViewHolder(private val binding: LayoutStoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, storyEntity: Story) {
            with(binding) {
                tvStoryUsername.text = storyEntity.name
                tvStoryDescription.text = storyEntity.description
                ivStoryImage.setImageFromUrl(context, storyEntity.photoUrl)
                tvStoryDate.setLocalDateFormat(storyEntity.createdAt)

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(tvStoryUsername, "story_image"),
                            Pair(tvStoryDescription, "username"),
                            Pair(ivStoryImage, "date"),
                            Pair(tvStoryDate, "description")
                        )

                    Intent(context, DetailStoryActivity::class.java).apply {
                        putExtra(EXTRA_DETAIL, storyEntity)
                        context.startActivity(this, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
                oldItem == newItem
        }
    }
}