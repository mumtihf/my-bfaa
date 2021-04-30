package com.mumti.consumerapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mumti.consumerapp.FollowersFragment
import com.mumti.consumerapp.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = username?.let { FollowingFragment.newInstance(it) }
            1 -> fragment = username?.let { FollowersFragment.newInstance(it) }
        }
        return fragment as Fragment
    }
}