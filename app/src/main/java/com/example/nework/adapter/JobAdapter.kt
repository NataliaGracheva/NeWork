package com.example.nework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nework.R
import com.example.nework.databinding.CardJobBinding
import com.example.nework.dto.Job

class JobAdapter(
    private val ownedByMe: Boolean,
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {
    interface OnInteractionListener {
        fun onEdit(job: Job)
        fun onRemove(job: Job)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(parent.context, binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, ownedByMe)
        }
    }
}

class JobViewHolder(
    private val context: Context,
    private val binding: CardJobBinding,
    private val onInteractionListener: JobAdapter.OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job, ownedByMe: Boolean) {

        binding.apply {
            textViewNameCardJob.text = job.name
            textViewPositionCardJob.text = job.position
            textViewStartCardJob.text = job.start
            textViewFinishCardJob.text =
                job.finish ?: context.getString(R.string.text_job_now)
            textViewLinkCardJob.visibility =
                if (job.link == null) GONE else VISIBLE
            textViewLinkCardJob.text = job.link

            buttonMenuCardJob.visibility = if (ownedByMe) VISIBLE else View.INVISIBLE

            buttonMenuCardJob.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(job)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(job)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}