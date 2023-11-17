package com.example.nework.ui

import android.content.Intent
import android.net.Uri
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
import com.example.nework.adapter.UserPostAdapter
import com.example.nework.auth.AppAuth
import com.example.nework.databinding.FragmentPostsBinding
import com.example.nework.dto.Post
import com.example.nework.viewmodel.WallViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WallFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    private val viewModel:WallViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)
        val adapter = UserPostAdapter(object : UserPostAdapter.OnInteractionListener {
            override fun onImageClick(post: Post) {
                try {
                    val uri = Uri.parse(post.attachment?.url)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "image/*")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onPlayVideo(post: Post) {
                try {
                    val uri = Uri.parse(post.attachment?.url)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "video/*")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onPlayAudio(post: Post) {
                try {
                    val uri = Uri.parse(post.attachment?.url)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "audio/*")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }

//            override fun onEdit(post: Post) {
//                viewModel.edit(post)
//                findNavController().navigate(R.id.action_postsFragment_to_newPostFragment,
//                    Bundle().apply {
//                        textArg = post.content
//                    })
//            }

//            override fun onLike(post: Post) {
//                if (auth.authStateFlow.value.id != 0L) {
//                    if (!post.likedByMe) viewModel.likeById(post.id)
//                    else viewModel.unlikeById(post.id)
//                } else {
//                    findNavController().navigate(R.id.signInFragment)
//                }
//            }

//            override fun onRemove(post: Post) {
//                viewModel.removeById(post.id)
//            }
        })

        val id = parentFragment?.arguments?.getLong("id")

        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            if (id != null) {
                viewModel.loadUserWall(id)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyText.isVisible = it.isEmpty()
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            when {
                it.error -> {
                    Toast.makeText(context, R.string.error_loading, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progress.isVisible = it.loading
        }

        return binding.root
    }
}