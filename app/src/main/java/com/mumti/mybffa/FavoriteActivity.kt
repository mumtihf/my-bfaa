package com.mumti.mybffa

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mumti.mybffa.adapter.CardViewUserAdapter
import com.mumti.mybffa.databinding.ActivityFavoriteBinding
import com.mumti.mybffa.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.mumti.mybffa.entity.User
import com.mumti.mybffa.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: CardViewUserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"

        showRecyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loadUserAsync()
    }

    private fun showRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        adapter = CardViewUserAdapter()
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : CardViewUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_PERSON, data)
                startActivity(intent)
            }
        })
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.proressBar.visibility = View.VISIBLE
            val defferedUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favorite = defferedUser.await()
            binding.proressBar.visibility = View.INVISIBLE
            if (favorite.size > 0) {
                adapter.mData = favorite
            } else {
                adapter.mData = ArrayList()
                showSnackBarMessage("Tidak ada data saat ini")
            }
            Log.d("BANYAK NYA DATA", favorite.toString())
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
}