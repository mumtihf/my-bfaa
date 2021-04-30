package com.mumti.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mumti.consumerapp.BuildConfig
import com.mumti.consumerapp.FollowingFragment
import com.mumti.consumerapp.entity.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception
import java.util.ArrayList

class FollowingViewModel: ViewModel() {
    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
    }

    val followingList = MutableLiveData<ArrayList<User>>()

    fun setFollowingUsername(username: String?){
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
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
                    followingList.postValue(listUser)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getFollowingList(): LiveData<ArrayList<User>>{
        return followingList
    }
}