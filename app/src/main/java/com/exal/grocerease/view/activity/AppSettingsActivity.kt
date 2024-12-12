package com.exal.grocerease.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ActivityAppSettingsBinding
import com.exal.grocerease.databinding.BottomSheetThemeBinding
import com.exal.grocerease.helper.manager.ThemeManager
import com.exal.grocerease.view.adapter.MenuAppSettingAdapter
import com.exal.grocerease.view.adapter.MenuItemApp
import com.google.android.material.bottomsheet.BottomSheetDialog

class AppSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menuItemsApp = listOf(
            MenuItemApp("Tema", R.drawable.ic_theme),
            MenuItemApp("Tentang Aplikasi", R.drawable.ic_info)
        )

        val adapter = MenuAppSettingAdapter(this, menuItemsApp)
        binding.listViewMenu.adapter = adapter

        binding.listViewMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> showThemeBottomSheet()
                1 -> {
                    val intent = Intent(this, AboutAppActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    private fun showThemeBottomSheet() {
        val bottomSheetBinding = BottomSheetThemeBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        val themeManager = ThemeManager(getSharedPreferences("app_prefs", MODE_PRIVATE))
        val isDarkModeEnabled = themeManager.isDarkModeEnabled()

        if (isDarkModeEnabled) {
            bottomSheetBinding.radioDarkMode.isChecked = true
        } else {
            bottomSheetBinding.radioLightMode.isChecked = true
        }

        bottomSheetBinding.btnSave.setOnClickListener {
            val isDarkModeSelected = bottomSheetBinding.radioDarkMode.isChecked
            themeManager.saveDarkModeEnabled(isDarkModeSelected)
            bottomSheetDialog.dismiss()

            applyTheme(isDarkModeSelected)
        }

        bottomSheetDialog.show()
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}