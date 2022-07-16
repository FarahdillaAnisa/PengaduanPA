package com.icha.layananpengaduanpa

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icha.layananpengaduanpa.databinding.ActivityMainBinding
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.login.LoginActivity

class MainActivity : Activity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var session: SessionManager

    companion object {
        const val EXTRA_USERNAME = "uname_msy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(applicationContext)
        session.checkLogin()

        val user: HashMap<String, String> = session.getUserDetails()

        val username: String = user.get(SessionManager.KEY_ID)!!
//        val password: String = user.get(SessionManager.KEY_PASSWORD)!!

        binding.unameTxt.setText(username)

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(applicationContext, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }
}