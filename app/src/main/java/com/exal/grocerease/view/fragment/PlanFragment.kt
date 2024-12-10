package com.exal.grocerease.view.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.exal.grocerease.databinding.FragmentPlanBinding
import com.exal.grocerease.helper.MonthYearPickerDialog
import com.exal.grocerease.view.activity.CreatePlanActivity
import com.exal.grocerease.view.activity.DetailExpenseActivity
import com.exal.grocerease.view.adapter.LoadingStateAdapter
import com.exal.grocerease.view.adapter.PlanAdapter
import com.exal.grocerease.viewmodel.PlanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlanFragment : Fragment() {
    private var _binding: FragmentPlanBinding? = null
    private val binding get() = _binding!!
    private val planViewModel: PlanViewModel by viewModels()
    private lateinit var pagingAdapter: PlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagingAdapter = PlanAdapter{ id, title, date ->
            navigateToDetail(id = id, title = title, date = date)
        }

        val loadingStateAdapter = LoadingStateAdapter { pagingAdapter.retry() }
        binding.rvPlan.adapter = pagingAdapter.withLoadStateFooter(
            footer = loadingStateAdapter
        )

        binding.rvPlan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlan.adapter = pagingAdapter

        lifecycleScope.launch {
            planViewModel.getLists("Plan")
            planViewModel.planList.observe(viewLifecycleOwner) { pagingData ->
                pagingAdapter.submitData(lifecycle, pagingData)
            }
        }

        lifecycleScope.launchWhenStarted {
            planViewModel.toastEvent.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.icCalender.setOnClickListener {
            MonthYearPickerDialog(requireContext()) { month, year ->
                lifecycleScope.launch {
                    val monthValue = month + 1
                    planViewModel.filterData("Plan", monthValue, year)
                    planViewModel.planList.observe(viewLifecycleOwner) { pagingData ->
                        pagingAdapter.submitData(lifecycle, pagingData)
                    }
                }
            }.show()
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), CreatePlanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToDetail(id: Int, title: String, date: String) {
        val intent = Intent(requireContext(), DetailExpenseActivity::class.java)
        val type = "Plan"
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