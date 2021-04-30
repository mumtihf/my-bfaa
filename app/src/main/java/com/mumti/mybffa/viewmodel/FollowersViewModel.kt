package com.mumti.mybffa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mumti.mybffa.BuildConfig
import com.mumti.mybffa.FollowersFragment
import com.mumti.mybffa.entity.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class FollowersViewModel: ViewModel() {
    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
    }

    val followerList = MutableLiveData<ArrayList<User>>()

    fun setFollowersUsername(username: String?){
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                val responseObject = JSONArray(result)
                Log.d(TAG, result)
                try {
                    for (i in 0 until responseObject.length()){
                        val item = responseObject.getJSONObject(i)

                        val user = User(
                            username = item.getString("login"),
                            photo = item.getString("avatar_url"),
                            url = item.getString("html_url")
                        )

                        listUser.add(user)
                    }
                    followerList.postValue(listUser)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getFollowersList() : LiveData<ArrayList<User>>{
        return followerList
    }
}