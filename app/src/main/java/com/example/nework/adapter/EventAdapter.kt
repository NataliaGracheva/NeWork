package com.example.nework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nework.R
import com.example.nework.databinding.CardEventBinding
import com.example.nework.dto.Event
import com.example.nework.enums.AttachmentType
import com.example.nework.view.load
import com.example.nework.view.loadCircleCrop

class EventAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<Event, EventViewHolder>(EventDiffCallback()) {

    interface OnInteractionListener {
        fun onEdit(event: Event)
        fun onRemove(event: Event)
        fun onLike(event: Event)
        fun onParticipate(event: Event)
        fun onOpenLikers(event: Event)
        fun onOpenParticipants(event: Event)
        fun onOpenSpeakers(event: Event)
        fun onAttachmentClick(event: Event)
        fun onAvatarClick(event: Event) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onInteractionListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onInteractionListener: EventAdapter.OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {

        binding.apply {
            if (event.authorAvatar != null) eventAuthorAvatar.loadCircleCrop(
                event.authorAvatar,
                R.drawable.baseline_account_circle_24
            )
            else eventAuthorAvatar.setImageResource(R.drawable.baseline_account_circle_24)
            eventAuthor.text = event.author
            eventPublished.text = event.published
            eventContent.text = event.content
            datetime.text = event.datetime
//            checkboxSpeakersSumCardEvent.text = event.speakerIds.count().toString()
            eventLike.isChecked = event.likedByMe
            eventLike.text = "${event.likeOwnerIds.size}"
            eventParticipate.isChecked = event.participatedByMe
            eventParticipate.text = "${event.participantsIds.size}"

            if (event.attachment != null) {
                when (event.attachment.type) {
                    AttachmentType.IMAGE -> {
                        eventAttachment.load(event.attachment.url, R.drawable.baseline_broken_image_24)
                        eventAttachment.visibility = View.VISIBLE
                    }

                    AttachmentType.VIDEO -> {
                        eventAttachment.load(event.attachment.url, R.drawable.baseline_video_file)
                        eventAttachment.visibility = View.VISIBLE
                    }

                    AttachmentType.AUDIO -> {
                        eventAttachment.setImageResource(R.drawable.baseline_audio_file)
                        eventAttachment.visibility = View.VISIBLE
                    }
                }
            }

            eventMenu.isVisible = event.ownedByMe
            eventMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, event.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(event)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(event)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            eventAttachment.setOnClickListener {
                onInteractionListener.onAttachmentClick(event)
            }

            eventAuthorAvatar.setOnClickListener {
                onInteractionListener.onAvatarClick(event)
            }

            eventLike.setOnClickListener {
                onInteractionListener.onLike(event)
            }

            eventParticipate.setOnClickListener {
                onInteractionListener.onParticipate(event)
            }


        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}