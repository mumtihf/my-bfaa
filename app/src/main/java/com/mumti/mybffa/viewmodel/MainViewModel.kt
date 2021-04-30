package com.mumti.mybffa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mumti.mybffa.BuildConfig
import com.mumti.mybffa.MainActivity
import com.mumti.mybffa.entity.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList

class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val listUsers = MutableLiveData<ArrayList<User>>()
    private val listDetailUsers = MutableLiveData<User>()

    fun setUser(users: String){
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$users"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)

                        val user = User(
                            id = item.getInt("id"),
                            username = item.getString("login"),
                            photo = item.getString("avatar_url"),
                            url = item.getString("html_url")
                        )

                        listUser.add(user)
                    }
                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>>{
        return listUsers
    }

    fun setDetailUser(users: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$users"
        client.addHeader("Authorization", "token.b6ffca07c4b5b6612add46c3624c81184fceca2f")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val item = JSONObject(result)

                    val user = User(
                        id = item.getInt("id"),
                        name = item.getString("name"),
                        photo = item.getString("avatar_url"),
                        username = item.getString("login"),
                        following = item.getString("following"),
                        followers = item.getString("followers"),
                        company = item.getString("company"),
                        location = item.getString("location"),
                        repository = item.getInt("public_repos")
                    )

                    listDetailUsers.postValue(user)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getDetailUser(): LiveData<User>{
        return listDetailUsers
    }
}