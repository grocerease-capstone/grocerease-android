package com.exal.grocerease.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.exal.grocerease.databinding.ActivityShareListBinding
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.view.adapter.SharedListAdapter
import com.exal.grocerease.viewmodel.ShareListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareListBinding
    private val viewModel: ShareListViewModel by viewModels()

    private lateinit var adapter: SharedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapter = SharedListAdapter(
            onItemClick = { id, title, boughtAt, type ->
                navigateToDetail(id, title, boughtAt, type)
            }
        )

        binding.rvShare.adapter = adapter
        binding.backBtn.setOnClickListener {
            finish()
        }
        rvSetup()
        observeSharedList()
        viewModel.getAllSharedList()
    }

    private fun rvSetup(){
        binding.rvShare.layoutManager = LinearLayoutManager(this)
    }

    private fun observeSharedList() {
        viewModel.shareList.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(resource.data?.data?.allDetailList)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigateToDetail(id: Int, title: String, date: String, typeDetail: String) {
        val intent = Intent(this, DetailExpenseActivity::class.java)
        val typeActivity = "Share"
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_ID, id)
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_TITLE, title)
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_DATE, date)
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_TYPE, typeDetail)
        intent.putExtra(DetailExpenseActivity.EXTRA_TYPE_ACTIVITY, typeActivity)
        Log.d("HomeFragment", "Expense ID: $id, Title: $title, Date: $date, Type: $typeDetail")
        startActivity(intent)
    }
}