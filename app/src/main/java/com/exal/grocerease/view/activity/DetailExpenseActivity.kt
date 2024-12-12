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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ActivityDetailExpenseBinding
import com.exal.grocerease.helper.DateFormatter
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.PostListResponse
import com.exal.grocerease.view.adapter.DetailExpenseAdapter
import com.exal.grocerease.view.fragment.ShareDialogFragment
import com.exal.grocerease.viewmodel.DetailExpenseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailExpenseBinding
    val viewModel: DetailExpenseViewModel by viewModels()

    private lateinit var expenseTitle: String
    private var listId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listId = intent.getIntExtra(EXTRA_EXPENSE_ID, -1)
        expenseTitle = intent.getStringExtra(EXTRA_EXPENSE_TITLE).toString()
        Log.d("DetailExpenseActivity", "Expense ID: $listId, Title: $expenseTitle")
        val expenseDate = intent.getStringExtra(EXTRA_EXPENSE_DATE).toString()
        val expenseType = intent.getStringExtra(EXTRA_EXPENSE_TYPE)

        var titleText = "Detail Expense"

        if (expenseType == "Plan") {
            titleText = "Detail Plan"
            binding.cardImage.visibility = View.GONE
            binding.shareBtn.visibility = View.GONE
        }

        binding.activityTxt.text = titleText

        binding.dateTv.text = DateFormatter.localizeDate(expenseDate ?: "")

        val expenseId = intent.getIntExtra(EXTRA_EXPENSE_ID, -1)
        if (expenseId != -1) {
            viewModel.getExpenseDetail(expenseId)
        } else {
            Toast.makeText(this, "Invalid expense ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        rvSetup()

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.titleTv.text = expenseTitle

        binding.editBtn.setOnClickListener {
            navigateToEditListDetail(expenseType)
        }

        binding.shareBtn.setOnClickListener {
            val shareDialog = ShareDialogFragment(expenseId)
            shareDialog.show(supportFragmentManager, "shareDialog")
        }

        binding.deleteBtn.setOnClickListener{
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.delete_list))
                .setMessage(resources.getString(R.string.list_will_be_removed))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.proceed)){ _, _ ->
                    lifecycleScope.launch {
                        viewModel.deleteExpense(expenseId).collect{ resource ->
                            handleResource(resource)
                        }
                    }
                }
                .show()
        }
    }

    fun handleShareResource(resource: Resource<PostListResponse>) {
        when (resource) {
            is Resource.Loading -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            is Resource.Success -> {
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            is Resource.Error -> {
                Log.d("share", resource.message.toString())
                Toast.makeText(this, "Error ${resource.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleResource(resource: Resource<PostListResponse>) {
        when (resource) {
            is Resource.Loading -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            is Resource.Success -> {
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            is Resource.Error -> {
                Toast.makeText(this, "Error ${resource.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToEditListDetail(type: String?) {
        val intent = Intent(this, EditListDetailActivity::class.java)
        intent.putExtra(EditListDetailActivity.EXTRA_EXPENSE_ID, listId)
        intent.putExtra(EditListDetailActivity.EXTRA_EXPENSE_TITLE, expenseTitle)
        intent.putExtra("list_type", type)

        val detailItems = viewModel.productList.value?.data?.data?.detailList?.productItems.orEmpty()
        val detailItems2 = detailItems.forEach {
            Log.d("DetailExpenseActivity", "Detail Item: ${it.toString()}")
            it?.category?.toInt()
        }
        Log.d("DetailExpenseActivity", "Detail Items: ${detailItems2}")
        val jsonList = Gson().toJson(detailItems)
        Log.d("DetailExpenseActivity", "JSON List: $jsonList")
        intent.putExtra(EditListDetailActivity.EXTRA_DETAIL_LIST, jsonList)

        startActivity(intent)
    }

    private fun rvSetup() {
        val adapter = DetailExpenseAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.itemRv.layoutManager = layoutManager
        binding.itemRv.adapter = adapter

        viewModel.productList.observe(this) {
            Log.d("DetailExpenseActivity", "Product List: ${it.data?.data?.detailList?.productItems}")
            adapter.submitList(it.data?.data?.detailList?.productItems)

            var totalPrice = 0
            it.data?.data?.detailList?.productItems?.forEach { item ->
                totalPrice += item?.totalPrice?.toDoubleOrNull()?.toInt() ?: 0
            }
            binding.totalPriceTv.text = "Total : " + rupiahFormatter(totalPrice)

            Glide.with(this)
                .load(it.data?.data?.detailList?.thumbnailImage)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.avatar)
                        .error(R.drawable.ic_close)
                )
                .into(binding.imageView)
        }
    }

    companion object {
        const val EXTRA_EXPENSE_ID = "extra_expense_id"
        const val EXTRA_EXPENSE_TITLE = "extra_expense_title"
        const val EXTRA_EXPENSE_DATE = "extra_expense_date"
        const val EXTRA_EXPENSE_TYPE = "extra_expense_type"
    }
}
