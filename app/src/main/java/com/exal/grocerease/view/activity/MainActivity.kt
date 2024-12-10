package com.exal.grocerease.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ActivityMainBinding
import com.exal.grocerease.helper.manager.IntroManager
import com.exal.grocerease.helper.manager.TokenManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var introManager: IntroManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("MainActivity", "Token: $token")

            // Log and toast
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

        if (!introManager.isIntroCompleted()) {
            val introValue = !introManager.isIntroCompleted()
            Log.d("MainActivity", "Intro not completed, starting IntroActivity $introValue")
            redirectToIntroActivity()
            return
        }

        if (!isUserLoggedIn()) {
            Log.d("IntroManager", "User not logged in, redirecting to LandingActivity")
            redirectToLandingActivity()
            return
        }

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

        val targetFragment = intent.getStringExtra("TARGET_FRAGMENT")
        if (targetFragment == "ExpensesFragment") {
            navController.navigate(R.id.expensesFragment)
        } else if (targetFragment == "PlanFragment") {
            navController.navigate(R.id.planFragment)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    private fun redirectToIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToLandingActivity() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
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
