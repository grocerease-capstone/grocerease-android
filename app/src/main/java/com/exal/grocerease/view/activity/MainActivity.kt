package com.exal.grocerease.view.activity

import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
        binding.bottomBar.itemIconTintActive = getColor(R.color.navIconColor)
        binding.bottomBar.barIndicatorColor = getColor(R.color.navIndicator)
        binding.bottomBar.setOnItemSelectedListener { position ->
            when (position) {
                0 -> navController.navigate(R.id.homeFragment)
                1 -> navController.navigate(R.id.expensesFragment)
                2 -> navController.navigate(R.id.planFragment)
                else -> navController.navigate(R.id.profileFragment)
            }
        }
    }
}
