package com.icha.layananpengaduanpa.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.icha.layananpengaduanpa.MainActivity
import com.icha.layananpengaduanpa.ui.login.LoginActivity

class SessionManager {
    var pref : SharedPreferences
    var editor: SharedPreferences.Editor
    var context: Context

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, 0)
        editor = pref.edit()
    }

    companion object {
        val PREF_NAME: String = "DataLogin"
        val IS_LOGIN: String = "isLogin"
        val KEY_USERNAME: String = "uname"
        val KEY_PASSWORD: String = "pass"
    }

    fun createLoginSession(uname: String, pass: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, uname)
        editor.putString(KEY_PASSWORD, pass)
        editor.commit()
    }

    fun isLoggedIn() : Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    fun getUserDetails(): HashMap<String, String> {
        var user : Map<String, String> = HashMap()
        (user as HashMap).put(KEY_USERNAME, pref.getString(KEY_USERNAME, null).toString())
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null).toString())
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }


}