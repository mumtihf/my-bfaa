package com.mumti.mybffa.helper

import android.database.Cursor
import com.mumti.mybffa.db.DatabaseContract
import com.mumti.mybffa.entity.User
import java.lang.Exception
import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val listUser = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.AVATAR_URL))
                listUser.add(User(
                    id = id,
                    username = username,
                    photo = photo
                ))
            }
        }
        return listUser
    }

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()
        try {
            userCursor?.apply {
                moveToFirst()
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.AVATAR_URL))
                user = User(
                        id = id,
                        username = username,
                        photo = photo
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return user
    }
}