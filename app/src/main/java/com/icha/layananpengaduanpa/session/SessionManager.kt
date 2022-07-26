package com.icha.layananpengaduanpa.session

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.icha.layananpengaduanpa.MenuMasyarakat
import com.icha.layananpengaduanpa.MenuOperator
import com.icha.layananpengaduanpa.MenuPolisi
import com.icha.layananpengaduanpa.MenuSpkt
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
        val KEY_ID: String = "id"
        val KEY_NAMA: String = "nama"
        val KEY_NOTELP: String = "notelp"
        val KEY_USERNAME: String = "uname"
        val KEY_PASSWORD: String = "pass"
        val KEY_SATWIL: String = "satwil"
        val ROLE_USER: String = "roleuser"

        val KEY_ADUAN: String = "kodeaduan"
        val KEY_SATWIL_ADUAN: String = "satwiladuan"
    }

    fun createLoginSession(pass: String, id: String, nama: String, notelp: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_PASSWORD, pass)
        editor.putString(KEY_ID, id)
        editor.putString(KEY_NAMA, nama)
        editor.putString(KEY_NOTELP, notelp)
        editor.putString(ROLE_USER, "masyarakat")
        editor.commit()
    }

    fun createLoginPolisiSession(pass: String, id: String, nama: String, notelp: String, satwil: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_PASSWORD, pass)
        editor.putString(KEY_ID, id)
        editor.putString(KEY_NAMA, nama)
        editor.putString(KEY_NOTELP, notelp)
        editor.putString(KEY_SATWIL, satwil)
        editor.putString(ROLE_USER, "polisi")
        editor.commit()
    }

    fun createLoginSpktSession(idSpkt: String, unameSpkt: String, satuanWilayah: String, notelpSpkt: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_ID, idSpkt)
        editor.putString(KEY_NAMA, unameSpkt)
        editor.putString(KEY_NOTELP, notelpSpkt)
        editor.putString(KEY_SATWIL, satuanWilayah)
        editor.putString(ROLE_USER, "spkt")
        editor.commit()
    }

    fun createLoginOperatorSession(idOperator: String, unameOperator: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_ID, idOperator)
        editor.putString(KEY_USERNAME, unameOperator)
        editor.putString(ROLE_USER, "operator")
        editor.commit()
    }

    fun isLoggedIn() : Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun getUserDetails(): HashMap<String, String> {
        var user : Map<String, String> = HashMap()
        (user as HashMap).put(KEY_ID, pref.getString(KEY_ID, null).toString())
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null).toString())
        user.put(KEY_NAMA, pref.getString(KEY_NAMA, null).toString())
        user.put(KEY_NOTELP, pref.getString(KEY_NOTELP, null).toString())
        user.put(KEY_SATWIL, pref.getString(KEY_SATWIL, null).toString())
        user.put(ROLE_USER, pref.getString(ROLE_USER, null).toString())
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null).toString())
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun simpanDataAduan(kodeAduan: String, satuanWilayah: String) {
        editor.putString(KEY_ADUAN, kodeAduan)
        editor.putString(KEY_SATWIL_ADUAN, satuanWilayah)
        editor.commit()
    }

    fun getSimpanAduan(): HashMap<String, String> {
        var aduan : Map<String, String> = HashMap()
        (aduan as HashMap).put(KEY_ADUAN, pref.getString(KEY_ADUAN, "Tidak Ada").toString())
        aduan.put(KEY_SATWIL_ADUAN, pref.getString(KEY_SATWIL_ADUAN, "Tidak Ada").toString())
        return aduan
    }
}