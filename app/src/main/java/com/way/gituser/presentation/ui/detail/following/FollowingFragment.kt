package com.way.gituser.presentation.ui.detail.following

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.way.gituser.databinding.FragmentFollowerBinding
import com.way.gituser.presentation.ui.detail.follower.FollowerFragment.Companion.USERNAME_ARGS
import com.way.gituser.presentation.ui.home.HomeAdapter
import com.way.gituser.presentation.ui.viewmodel.DetailViewModel
import com.way.gituser.presentation.ui.viewmodel.DetailViewModel.Companion.USER_FOLLOWING
import com.way.gituser.presentation.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var detailViewModel: DetailViewModel
    private var usernameArgs: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments.let {
            usernameArgs = it?.getString(USERNAME_ARGS).toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

            if (usernameArgs.isNotEmpty()) {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        showLoading(true)
                        delay(1000)
                        detailViewModel.getFollowingUser(usernameArgs)
                        viewFollowingUser()
                    }
                }
                initRecyclerView()
            }
        }
    }

    private fun viewFollowingUser() {
        detailViewModel.userFollowing.observe(requireParentFragment().viewLifecycleOwner) { response ->
            try {
                showLoading(false)
                response.body().let {
                    homeAdapter.differ.submitList(it)
                    Log.e(USER_FOLLOWING, it?.size.toString())
                }
            } catch (e: Exception) {
                showLoading(false)
                showError(e.message.toString(), view)
                Log.e(USER_FOLLOWING, e.message.toString())
            }
        }
    }

    private fun initRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvFollow.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressBar.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showError(text: String, view: View?) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(usernameArgs: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME_ARGS, usernameArgs)
                }
            }
    }
}