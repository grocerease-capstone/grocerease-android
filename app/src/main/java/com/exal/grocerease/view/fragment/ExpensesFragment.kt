package com.exal.grocerease.view.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.exal.grocerease.R
import com.exal.grocerease.databinding.FragmentExpensesBinding
import com.exal.grocerease.helper.MonthYearPickerDialog
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.view.activity.CreateListActivity
import com.exal.grocerease.view.activity.DetailExpenseActivity
import com.exal.grocerease.view.adapter.ExpensesAdapter
import com.exal.grocerease.view.adapter.LoadingStateAdapter
import com.exal.grocerease.viewmodel.ExpensesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols

@AndroidEntryPoint
class ExpensesFragment : Fragment() {
    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private val expenseViewModel: ExpensesViewModel by viewModels()
    private lateinit var pagingAdapter: ExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagingAdapter = ExpensesAdapter{ id, title, date ->
            navigateToDetail(id = id, title = title, date = date)
        }
        binding.rvExpense.layoutManager = LinearLayoutManager(context)
        binding.rvExpense.adapter = pagingAdapter

        val loadingStateAdapter = LoadingStateAdapter { pagingAdapter.retry() }
        binding.rvExpense.adapter = pagingAdapter.withLoadStateFooter(
            footer = loadingStateAdapter
        )

        lifecycleScope.launch {
            expenseViewModel.getLists("Track", null, null)
            expenseViewModel.expenses.observe(viewLifecycleOwner) { pagingData ->
                pagingAdapter.submitData(lifecycle, pagingData)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateListActivity::class.java)
            startActivity(intent)
        }

        binding.icCalender.setOnClickListener {
            MonthYearPickerDialog(requireContext()) { month, year ->
                lifecycleScope.launch {
                    val monthValue = month + 1
                    Log.d("month", "Bulan saat ini : $month   ||   $monthValue")
                    expenseViewModel.getLists("Track", monthValue, year)
                    expenseViewModel.expenses.observe(viewLifecycleOwner) { pagingData ->
                        pagingAdapter.submitData(lifecycle, pagingData)
                    }
                }
            }.show()
        }
    }

    private fun navigateToDetail(id: Int, title: String, date: String) {
        val intent = Intent(requireContext(), DetailExpenseActivity::class.java)
        val type = "Detail Expense"
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_ID, id)
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_TITLE, title)
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_DATE, date)
        intent.putExtra(DetailExpenseActivity.EXTRA_EXPENSE_TYPE, type)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}