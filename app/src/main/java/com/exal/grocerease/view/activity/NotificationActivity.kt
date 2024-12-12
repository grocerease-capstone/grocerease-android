package com.exal.grocerease.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ActivityNotificationBinding
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.view.adapter.NotificationAdapter
import com.exal.grocerease.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = NotificationAdapter(viewModel)

        setupRecyclerView()

        binding.backBtn.setOnClickListener {
            finish()
        }
        observeList()
        observeAccept()
        observeDecline()
        viewModel.getNotificationList()
    }

    private fun setupRecyclerView() {
        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvNotification.addItemDecoration(itemDecoration)
    }

    private fun observeList(){
        viewModel.notificationList.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    adapter.submitList(resource.data?.data?.requestDetail)
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(resource.message)
                }
            }
        }
    }

    private fun observeDecline(){
        viewModel.declineNotification.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    viewModel.getNotificationList()
                    showMessage("Daftar berhasil ditolak")
                }

                is Resource.Error -> {
                    showLoading(false)
                    showError(resource.message)
                }
            }
        }
    }

    private fun observeAccept(){
        viewModel.acceptNotification.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    viewModel.getNotificationList()
                    showMessage("Daftar berhasil diterima")
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(resource.message)
                }
            }
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message ?: "Unknown error occurred", Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
}