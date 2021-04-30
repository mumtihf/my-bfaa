package com.mumti.mybffa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mumti.mybffa.adapter.CardViewUserAdapter
import com.mumti.mybffa.databinding.ActivityMainBinding
import com.mumti.mybffa.entity.User
import com.mumti.mybffa.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CardViewUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private var title: String = "Search Github User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBarTitle()

        showRecyclerCardView()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.btnSearch.setOnClickListener {
            val username = binding.searchText.text.toString()
            if (username.isEmpty()) return@setOnClickListener
            showLoading(true)
            mainViewModel.setUser(username)
        }

        mainViewModel.getUser().observe(this, {user ->
            if (user != null){
                adapter.setData(user)
                showLoading(false)
            }
        })
    }

    private fun setActionBarTitle() {
        supportActionBar?.title = title
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.proressBar.visibility = View.VISIBLE
        } else {
            binding.proressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerCardView(){
        adapter = CardViewUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : CardViewUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                //binding.proressBar.visibility = View.VISIBLE
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_PERSON, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(this, SettingActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}