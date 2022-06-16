package com.way.gituser.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.gituser.R
import com.way.gituser.databinding.FragmentHomeBinding
import com.way.gituser.presentation.ui.detail.follower.FollowerFragment.Companion.USERNAME_ARGS
import com.way.gituser.presentation.ui.viewmodel.HomeViewModel
import com.way.gituser.presentation.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        MainScope().launch(Dispatchers.Main) {
            showLoadingView()
            delay(1000)
            homeViewModel.getRandomUser()
            viewRandomUser()
        }

        binding.ivSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString()
            if (query.isEmpty()) {
                MainScope().launch(Dispatchers.Main) {
                    showLoadingView()
                    delay(1000)
                    hideLoadingView()
                    hideRecyclerView()
                }
            } else {
                MainScope().launch(Dispatchers.Main) {
                    showLoadingView()
                    delay(1000)
                    homeViewModel.getSearchedUser(query)
                    viewSearchedUser()
                }
            }
        }

        initRecyclerView()

        homeAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString(USERNAME_ARGS, it.login)
            }
            activity?.findNavController(R.id.fragmentContainerView2)
                ?.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }

    private fun viewRandomUser() {
        homeViewModel.randomUser.observe(requireParentFragment().viewLifecycleOwner) { response ->
            try {
                hideLoadingView()
                showRecyclerView()
                response.body().let {
                    homeAdapter.differ.submitList(it)
                    Log.e(HomeViewModel.SEARCH, it?.toList()?.size.toString())
                }
            } catch (e: Exception) {
                hideLoadingView()
                Log.e(HomeViewModel.SEARCH, e.message.toString())
            }
        }
    }

    private fun viewSearchedUser() {
        homeViewModel.search.observe(viewLifecycleOwner) { response ->
            try {
                hideLoadingView()
                showRecyclerView()
                response.body().let {
                    homeAdapter.differ.submitList(it?.items?.toList())
                    Log.e(HomeViewModel.SEARCH, it?.items?.toList()?.size.toString())
                }
            } catch (e: Exception) {
                hideLoadingView()
                Log.e(HomeViewModel.SEARCH, e.message.toString())
            }
        }
    }

    private fun initRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvUser.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoadingView() {
        binding.includeLoading.viewLoading.visibility = VISIBLE
    }

    private fun hideLoadingView() {
        binding.includeLoading.viewLoading.visibility = INVISIBLE
    }

    private fun showRecyclerView() {
        binding.rvUser.visibility = VISIBLE
    }

    private fun hideRecyclerView() {
        binding.rvUser.visibility = INVISIBLE
    }
}