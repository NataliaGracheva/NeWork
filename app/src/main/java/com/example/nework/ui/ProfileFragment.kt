package com.example.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nework.R
import com.example.nework.adapter.ProfileAdapter
import com.example.nework.databinding.FragmentProfileBinding
import com.example.nework.view.loadCircleCrop
import com.example.nework.viewmodel.AuthViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val authViewModel by activityViewModels<AuthViewModel>()

    private val profileTitles = arrayOf(
        R.string.title_posts,
        R.string.title_jobs
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )

        val viewPagerProfile = binding.viewPagerFragmentProfile
        val tabLayoutProfile = binding.tabLayoutFragmentProfile
        val id = arguments?.getLong("id")
        val avatar = arguments?.getString("avatar")
        val name = arguments?.getString("name")

        viewPagerProfile.adapter = ProfileAdapter(this)

        TabLayoutMediator(tabLayoutProfile, viewPagerProfile) { tab, position ->
            tab.text = getString(profileTitles[position])
        }.attach()

        with(binding) {
            textViewUserNameFragmentProfile.text = name?.removeSurrounding("\"")
            if (avatar != null) {
                imageViewUserAvatarFragmentProfile.loadCircleCrop(avatar, R.drawable.baseline_account_circle_24)
            } else {
                imageViewUserAvatarFragmentProfile.setImageResource(R.drawable.baseline_account_circle_24)
            }
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            if (authViewModel.authenticated && id == it.id) {
                // todo - add actions
            }
        }

        return binding.root
    }
}