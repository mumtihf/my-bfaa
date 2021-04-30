package com.mumti.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mumti.consumerapp.BuildConfig
import com.mumti.consumerapp.entity.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val listDetailUsers = MutableLiveData<User>()

    fun setDetailUser(users: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$users"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)

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