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
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.adapter.UserPostAdapter
import com.example.nework.auth.AppAuth
import com.example.nework.databinding.FragmentPostsBinding
import com.example.nework.dto.Post
import com.example.nework.ui.NewPostFragment.Companion.textArg
import com.example.nework.viewmodel.PostViewModel
import com.example.nework.viewmodel.WallViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WallFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    private val viewModel: WallViewModel by activityViewModels()
    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)
        val id = parentFragment?.arguments?.getLong("id")
        val ownedByMe = auth.authStateFlow.value.id == id

        val adapter = UserPostAdapter(ownedByMe, object : UserPostAdapter.OnInteractionListener {
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

            override fun onEdit(post: Post) {
                postViewModel.edit(post)
                post.attachment?.let {
                    postViewModel.changeMedia(Uri.parse(it.url), null, it.type)
                }
                findNavController().navigate(R.id.action_wallFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
            }

            override fun onLike(post: Post) {
                if (auth.authStateFlow.value.id != 0L) {
                    if (!post.likedByMe) viewModel.likeById(post.id)
                    else viewModel.unlikeById(post.id)
                } else {
                    findNavController().navigate(R.id.signInFragment)
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

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

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) {
                        if (id != null) {
                            viewModel.loadUserWall(id)
                        }
                    }
                    .show()
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            if (id != null) {
                viewModel.loadUserWall(id)
            }
        }

        binding.fab.isVisible = ownedByMe

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_wallFragment_to_newPostFragment)
        }

        return binding.root
    }
}