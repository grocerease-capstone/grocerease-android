package com.exal.grocerease.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exal.grocerease.databinding.ActivityLandingBinding
import com.exal.grocerease.helper.manager.IntroManager
import com.exal.grocerease.helper.manager.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var introManager: IntroManager

    @Inject
    lateinit var tokenManager: TokenManager

    private var binding: ActivityLandingBinding? = null
    private val _binding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        _binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        _binding.helpBtn.setOnClickListener {
            introManager.clearIntroFlag()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(tokenManager.isLoggedIn()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}