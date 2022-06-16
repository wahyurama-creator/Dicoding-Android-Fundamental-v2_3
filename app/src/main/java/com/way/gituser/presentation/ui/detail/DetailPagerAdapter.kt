package com.way.gituser.presentation.ui.detail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.way.gituser.presentation.ui.detail.follower.FollowerFragment
import com.way.gituser.presentation.ui.detail.following.FollowingFragment

class DetailPagerAdapter(fragment: Fragment, private val usernameArgs: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = when (position) {
            0 -> FollowerFragment.newInstance(usernameArgs)
            1 -> FollowingFragment.newInstance(usernameArgs)
            else -> FollowerFragment.newInstance(usernameArgs)
        }
        return fragment
    }
}