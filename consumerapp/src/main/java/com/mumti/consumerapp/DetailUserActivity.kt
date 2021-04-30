package com.mumti.consumerapp

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mumti.consumerapp.adapter.SectionPagerAdapter
import com.mumti.consumerapp.databinding.ActivityDetailUserBinding
import com.mumti.consumerapp.db.DatabaseContract.NoteColumns.Companion.AVATAR_URL
import com.mumti.consumerapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.mumti.consumerapp.db.DatabaseContract.NoteColumns.Companion.USERNAME
import com.mumti.consumerapp.db.DatabaseContract.NoteColumns.Companion._ID
import com.mumti.consumerapp.entity.User
import com.mumti.consumerapp.helper.MappingHelper
import com.mumti.consumerapp.viewModel.MainViewModel
import kotlinx.coroutines.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: MainViewModel
    private var title: String = "User Detail"

    private var statusFavorite = false
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_PERSON = "extra_person"

        @StringRes
        private val TAB_TITLE = intArrayOf(
                R.string.following,
                R.string.followers
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBarTitle(title)

        val user = intent.getParcelableExtra<User>(EXTRA_PERSON) as User

        Log.d("USER DATA: ", user.toString())

        getData()

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)

        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            val userData = MappingHelper.mapCursorToObject(cursor)
            Log.d("USER: " , userData.toString())

            if (userData.id == 0) {
                statusFavorite = false
                setStatusFavorite(statusFavorite)
            } else {
                statusFavorite = true
                setStatusFavorite(statusFavorite)
            }
            cursor.close()
        }

        binding.fab.setOnClickListener {
            addFav(user)
        }
    }

    private fun addFav(user: User) {
        if (statusFavorite) {
            val username = user.username.toString()
            contentResolver.delete(uriWithId, username, null)
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)

            Toast.makeText(this, "Unfavorite", Toast.LENGTH_SHORT).show()
        } else {
            val values = ContentValues()
            values.put(_ID, user.id)
            values.put(USERNAME, user.username)
            values.put(AVATAR_URL, user.photo)

            contentResolver.insert(CONTENT_URI, values)
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)

            Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            val favYes: Int = R.drawable.ic_baseline_favorite_24
            binding.fab.setImageResource(favYes)
        } else {
            val favNo: Int = R.drawable.ic_baseline_favorite_border_24
            binding.fab.setImageResource(favNo)
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.proressBar.visibility = View.VISIBLE
        } else {
            binding.proressBar.visibility = View.GONE
        }
    }

    private fun getData() {
        val user = intent.getParcelableExtra<User>(EXTRA_PERSON) as User

        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MainViewModel::class.java)
        user.username.let {
            if (it != null) {
                detailUserViewModel.setDetailUser(it)
                showLoading(true)
            }
        }

        detailUserViewModel.getDetailUser().observe(this, {
            binding.apply {
                showLoading(false)
                Glide.with(this@DetailUserActivity)
                        .load(it.photo)
                        .apply(RequestOptions().override(200,120))
                        .into(imgUserPhoto)

                tvName.text = it.name
                username.text = it.username
                tvCompany.text = it.company
                tvLocation.text = it.location
                tvFollowing.text = it.following
                tvFollowers.text = it.followers
                tvRepository.text = it.repository.toString()
            }
        })

        //view pager
        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = user.username //setter dari pageradapter
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
}