package com.example.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.nework.R
import com.example.nework.adapter.JobAdapter
import com.example.nework.databinding.FragmentJobsBinding
import com.example.nework.viewmodel.JobViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobsFragment : Fragment() {
    private val viewModel by activityViewModels<JobViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentJobsBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = JobAdapter()

        val id = parentFragment?.arguments?.getLong("id")

        binding.recyclerViewContainerFragmentJobs.adapter = adapter

        lifecycleScope.launchWhenCreated {
            if (id != null) {
                viewModel.loadJobs(id)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.textViewEmptyTextFragmentJobs.isVisible = it.isEmpty()
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            when {
                it.error -> {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBarFragmentJobs.isVisible = it.loading
        }

        return binding.root
    }
}