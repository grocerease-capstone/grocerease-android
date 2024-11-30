package com.exal.grocerease.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exal.grocerease.databinding.ActivityLandingBinding
import com.exal.grocerease.helper.manager.IntroManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var introManager: IntroManager

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
}