package com.mumti.mybffa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mumti.mybffa.adapter.CardViewUserAdapter
import com.mumti.mybffa.databinding.FragmentFollowersBinding
import com.mumti.mybffa.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {

    private var adapter = CardViewUserAdapter()
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowersViewModel

    companion object {
        private val USERNAME = "username"

        fun newInstance(username: String): FollowersFragment{
            val fragment = FollowersFragment()
            val mBundle = Bundle()
            mBundle.putString(USERNAME, username)
            fragment.arguments = mBundle
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(USERNAME)
        showRecyclerList()

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        showLoading(true)
        followersViewModel.setFollowersUsername(username)
        followersViewModel.getFollowersList().observe(viewLifecycleOwner, { followerList ->
            if (followerList != null){
                adapter.setData(followerList)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList(){
        adapter = CardViewUserAdapter()
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapter
        binding.rvFollowers.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.proressBar.visibility = View.VISIBLE
        } else {
            binding.proressBar.visibility = View.GONE
        }
    }
}