package com.way.gituser.presentation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.gituser.R
import com.way.gituser.databinding.FragmentFavoriteBinding
import com.way.gituser.presentation.ui.detail.follower.FollowerFragment.Companion.USERNAME_ARGS
import com.way.gituser.presentation.ui.viewmodel.FavoriteViewModel
import com.way.gituser.presentation.ui.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        initRecyclerView()
        viewFavoriteUser()
    }

    private fun initRecyclerView() {
        favoriteAdapter = FavoriteAdapter()
        binding.rvUser.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context)
        }
        favoriteAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString(USERNAME_ARGS, it.username)
            }
            binding.root.findNavController()
                .navigate(R.id.action_favoriteFragment_to_detailFragment, bundle)
        }
    }

    private fun viewFavoriteUser() {
        favoriteViewModel.getFavoriteUser().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                isShowEmptyView(true)
            } else {
                isShowEmptyView(false)
                favoriteAdapter.differ.submitList(it)
            }
        }
    }

    private fun isShowEmptyView(state: Boolean) {
        if (state) {
            binding.ivFavorite.visibility = VISIBLE
            binding.tvEmpty.visibility = VISIBLE
            binding.ivFavorite.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.beat
                )
            )
        } else {
            binding.ivFavorite.visibility = GONE
            binding.tvEmpty.visibility = GONE
        }
    }
}