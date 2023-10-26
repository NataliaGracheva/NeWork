package com.example.nework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nework.R
import com.example.nework.databinding.CardPostBinding
import com.example.nework.dto.Post
import com.example.nework.enums.AttachmentType
import com.example.nework.view.load
import com.example.nework.view.loadCircleCrop


class PostAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    interface OnInteractionListener {
        fun onImageClick(post: Post) {}
        fun onPlayVideo(post: Post) {}
        fun onPlayAudio(post: Post) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: PostAdapter.OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (post.authorAvatar != null) avatar.loadCircleCrop(
                post.authorAvatar,
                R.drawable.baseline_account_circle_24
            )
            else avatar.setImageResource(R.drawable.baseline_account_circle_24)
            like.isChecked = post.likedByMe
            like.text = "${post.likeOwnerIds.size}"
            if (post.attachment != null) {
                when (post.attachment.type) {
                    AttachmentType.IMAGE -> {
                        image.load(post.attachment.url, R.drawable.baseline_broken_image_24)
                        image.visibility = View.VISIBLE
                    }
                    AttachmentType.VIDEO -> {
                        video.setImageResource(R.drawable.baseline_video_file)
                        video.visibility = View.VISIBLE
                    }
                    AttachmentType.AUDIO -> {
                        audio.setImageResource(R.drawable.baseline_audio_file)
                        audio.visibility = View.VISIBLE
                    }
                }
            }

            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)

                }.show()
            }

            image.setOnClickListener {
                onInteractionListener.onImageClick(post)
            }

            video.setOnClickListener {
                onInteractionListener.onPlayVideo(post)
            }

            audio.setOnClickListener {
                onInteractionListener.onPlayAudio(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
