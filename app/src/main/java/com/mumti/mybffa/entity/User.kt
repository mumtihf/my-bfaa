package com.mumti.mybffa.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        var id: Int = 0,
        var name: String? = null,
        var following: String? = null,
        var followers: String? = null,
        var photo: String? = null,
        var username: String? = null,
        var url: String? = null,
        var company: String? = null,
        var location: String? = null,
        var repository: Int = 0
) : Parcelable
