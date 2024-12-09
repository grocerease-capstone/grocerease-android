package com.exal.grocerease.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.view.menu.MenuAdapter
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.exal.grocerease.R
import com.exal.grocerease.databinding.FragmentProfileBinding
import com.exal.grocerease.helper.MonthYearPickerDialog
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.helper.compose.LineSample
import com.exal.grocerease.helper.manager.TokenManager
import com.exal.grocerease.view.activity.AppSettingsActivity
import com.exal.grocerease.view.activity.LandingActivity
import com.exal.grocerease.view.adapter.MenuItem
import com.exal.grocerease.view.adapter.MenuProfileAdapter
import com.exal.grocerease.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormatSymbols
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        val menuItems = listOf(
            MenuItem("Account Settings", R.drawable.ic_account_settings),
            MenuItem("App Setting", R.drawable.ic_setting),
            MenuItem("Logout", R.drawable.ic_logout)
        )

        val adapter = MenuProfileAdapter(requireContext(), menuItems)
        binding.listViewMenu.adapter = adapter

        binding.listViewMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> Toast.makeText(requireContext(), "Account Settings clicked", Toast.LENGTH_SHORT).show()
                1 -> {
                    val intent = Intent(requireContext(), AppSettingsActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    viewModel.logout()
                    logoutObserver()
                }
            }
        }

        binding.composeView.setContent {
            MaterialTheme {
                LineSample(viewModel)
            }
        }

        binding.calendarButton.setOnClickListener {
            MonthYearPickerDialog(requireContext()) { month, year ->
                val monthName = DateFormatSymbols().months[month]
                val monthValue = month + 1
                Log.d("month", "Bulan saat ini : $month  ||  $monthValue")
                binding.dateTxt.text = "$monthName $year"
            }.show()
        }

//        binding.accSetting.setOnClickListener {
//            val intent = Intent(requireContext(), AppSettingsActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun logoutObserver() {
        viewModel.logoutState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    tokenManager.clearToken()
                    viewModel.clearDatabase()
                    val intent = Intent(requireContext(), LandingActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
