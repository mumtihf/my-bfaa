package com.mumti.consumerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mumti.consumerapp.adapter.CardViewUserAdapter
import com.mumti.consumerapp.databinding.FragmentFollowingBinding
import com.mumti.consumerapp.viewModel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var adapter = CardViewUserAdapter()
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        private val USERNAME = "username"

        fun newInstance(username: String): FollowingFragment{
            val fragment = FollowingFragment()
            val mBundle = Bundle()
            mBundle.putString(USERNAME, username)
            fragment.arguments = mBundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(USERNAME)
        showRecyclerList()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        showLoading(true)
        followingViewModel.setFollowingUsername(username)
        followingViewModel.getFollowingList().observe(viewLifecycleOwner, { followingList ->
            if (followingList != null){
                adapter.setData(followingList)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList(){
        adapter = CardViewUserAdapter()
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
        binding.rvFollowing.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.proressBar.visibility = View.VISIBLE
        } else {
            binding.proressBar.visibility = View.GONE
        }
    }
}