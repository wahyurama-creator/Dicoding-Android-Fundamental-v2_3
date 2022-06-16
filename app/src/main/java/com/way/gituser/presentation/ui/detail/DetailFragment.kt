package com.way.gituser.presentation.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.way.gituser.R
import com.way.gituser.data.remote.response.User
import com.way.gituser.databinding.FragmentDetailBinding
import com.way.gituser.presentation.ui.viewmodel.DetailViewModel
import com.way.gituser.presentation.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var usernameArgs: String
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usernameArgs = args.usernameArgs
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        setupBottomSheet()
        setupViewPager()

        MainScope().launch {
            showLoading(true)
            delay(300)
            detailViewModel.getUserDetail(usernameArgs)
            viewGetUserDetail()
        }

        favorite()

        binding.apply {
            ivBack.setOnClickListener {
                activity?.onBackPressed()
            }
            ivShare.setOnClickListener {
                shareIntent(getString(R.string.share_text, usernameArgs))
            }
        }
    }

    private fun setupViewPager() {
        val detailPagerAdapter = DetailPagerAdapter(this, usernameArgs)
        binding.layoutBottomSheet.apply {
            viewPagerUserDetail.adapter = detailPagerAdapter
            viewPagerUserDetail.offscreenPageLimit = 2
            TabLayoutMediator(
                tabLayout,
                viewPagerUserDetail
            ) { tab, position ->
                tab.text = getString(TAB_TITLE[position])
            }.attach()
        }
    }

    private fun setupBottomSheet() {
        BottomSheetBehavior.from(binding.layoutBottomSheet.viewBottomSheet).apply {
            this.state = BottomSheetBehavior.STATE_COLLAPSED
            setPeekHeight(240, true)
        }
    }

    private fun viewGetUserDetail() {
        detailViewModel.userDetail.observe(viewLifecycleOwner) { response ->
            try {
                showLoading(false)
                response.body().let {
                    if (it != null) {
                        //apply user to user entity
                        user = it

                        binding.apply {
                            tvUsername.text = it.username ?: getString(R.string.unavailable)
                            tvFullName.text = it.name ?: getString(R.string.unavailable)
                            tvCompany.text = it.company ?: getString(R.string.unavailable)
                            tvLocation.text = it.location ?: getString(R.string.unavailable)
                            tvFollowing.text = it.following.toString()
                            tvFollower.text = it.followers.toString()
                            tvRepository.text = it.repository.toString()
                            Glide.with(ivUser)
                                .load(it.avatarUrl)
                                .into(ivUser)
                        }
                    }
                }
            } catch (e: Exception) {
                showLoading(false)
                showError(e.message.toString(), view)
            }
        }
    }

    private fun favorite() {
        var favorite = false
        usernameArgs.let { detailViewModel.checkIsFavorite(it) }
        detailViewModel.isFavorite.observe(viewLifecycleOwner) {
            if (it) {
                favorite = true
                setImageFavorite(true)
            } else {
                favorite = false
                setImageFavorite(false)
            }
        }
        binding.fabFavorite.setOnClickListener {
            if (usernameArgs.isNotEmpty()) {
                favorite = if (favorite) {
                    detailViewModel.deleteUserFavorite(usernameArgs)
                    setImageFavorite(false)
                    false
                } else {
                    detailViewModel.setUserFavorite(user)
                    setImageFavorite(true)
                    true
                }
            }
        }
    }

    private fun shareIntent(text: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, "Share with: "))
    }

    private fun setImageFavorite(state: Boolean) {
        binding.fabFavorite.setImageDrawable(
            if (state) {
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_favorite_fill,
                    null
                )
            } else {
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_favorite_border,
                    null
                )
            }
        )
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        includeLoading.viewLoading.visibility = if (isShow) {
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
        @StringRes
        private val TAB_TITLE = intArrayOf(R.string.follower, R.string.following)
    }
}