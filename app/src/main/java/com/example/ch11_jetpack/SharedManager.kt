package com.example.ch11_jetpack

import android.content.Context
import android.content.SharedPreferences
import com.example.ch11_jetpack.PreferenceHelper.set
import com.example.ch11_jetpack.PreferenceHelper.get

class SharedManager(context: Context) {

    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveCurrentUser(user: User) {
        prefs["datedb"] = user.datedb
        prefs["age"] = user.age
        //prefs["email"] = user.email
        //prefs["password"] = user.password
        //prefs["isMarried"] = user.isMarried
    }

    fun getCurrentUser(): User {
        return User().apply {
            datedb = prefs["datedb", ""]
            age = prefs["age", 0]
            //email = prefs["email", ""]
            //password = prefs["password", ""]
            //isMarried = prefs["isMarried", false]
        }
    }

}