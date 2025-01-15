package com.example.dinus.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId)
        editor.commit()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("user_id", -1)
    }

    fun clearUserId() {
        val editor = sharedPreferences.edit()
        editor.remove("user_id")
        editor.apply()
    }
}
