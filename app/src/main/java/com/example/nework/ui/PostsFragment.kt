package com.example.nework.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.nework.R
import com.example.nework.adapter.PostAdapter
import com.example.nework.auth.AppAuth
import com.example.nework.databinding.FragmentPostsBinding
import com.example.nework.dto.Post
import com.example.nework.ui.NewPostFragment.Companion.textArg
import com.example.nework.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostsFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(object : PostAdapter.OnInteractionListener {
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
                viewModel.edit(post)
                findNavController().navigate(R.id.action_postsFragment_to_newPostFragment,
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest(adapter::submitData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { state ->
                    binding.swiperefresh.isRefreshing =
                        state.refresh is LoadState.Loading ||
                                state.prepend is LoadState.Loading ||
                                state.append is LoadState.Loading
                }
            }
        }

        binding.swiperefresh.setOnRefreshListener(adapter::refresh)

        binding.fab.setOnClickListener {
            if (auth.authStateFlow.value.id != 0L)
                findNavController().navigate(R.id.action_postsFragment_to_newPostFragment)
            else findNavController().navigate(R.id.signInFragment)
        }

        return binding.root
    }
}