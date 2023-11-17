package com.example.nework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nework.R
import com.example.nework.databinding.CardJobBinding
import com.example.nework.dto.Job

class JobAdapter : ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class JobViewHolder(
    private val context: Context,
    private val binding: CardJobBinding,
//    private val onJobInteractionListener: OnJobInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job) {

        binding.apply {
            textViewNameCardJob.text = job.name
            textViewPositionCardJob.text = job.position
            textViewStartCardJob.text = job.start
            textViewFinishCardJob.text =
                job.finish ?: context.getString(R.string.text_job_now)
            textViewLinkCardJob.visibility =
                if (job.link == null) GONE else VISIBLE
            textViewLinkCardJob.text = job.link

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