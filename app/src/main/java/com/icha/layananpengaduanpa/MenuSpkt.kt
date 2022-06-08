package com.icha.layananpengaduanpa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuSpkt : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_spkt)
        val navView: BottomNavigationView = findViewById(R.id.nav_view_spkt)

        val navController = findNavController(R.id.nav_host_fragment_spkt)
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_beranda_spkt, R.id.navigation_pengaduan_spkt, R.id.navigation_akun_spkt
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}