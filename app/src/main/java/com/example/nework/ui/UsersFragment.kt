package com.example.nework.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.adapter.OnUserInteractionListener
import com.example.nework.adapter.UserAdapter
import com.example.nework.databinding.FragmentUsersBinding
import com.example.nework.dto.User
import com.example.nework.viewmodel.UserViewModel

class UsersFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUsersBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = UserAdapter(object : OnUserInteractionListener {
            override fun openProfile(user: User) {
                Log.d("Users", "open profile")
//                findNavController().navigate(R.id.)
            }
        })
                binding.fragmentListUsers.adapter = adapter

                viewModel.data.observe(viewLifecycleOwner)
                {
                    adapter.submitList(it)
                }

                viewModel.dataState.observe(viewLifecycleOwner)
        {
            when {
                it.error -> {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBarFragmentUsers.isVisible = it.loading
        }
            return binding.root
    }
}