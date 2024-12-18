package com.exal.grocerease.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.exal.grocerease.R
import com.exal.grocerease.databinding.FragmentProfileBinding
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.helper.compose.LineSample
import com.exal.grocerease.helper.manager.EmailManager
import com.exal.grocerease.helper.manager.TokenManager
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.view.activity.AccountSettingsActivity
import com.exal.grocerease.view.activity.AppSettingsActivity
import com.exal.grocerease.view.activity.LandingActivity
import com.exal.grocerease.view.adapter.MenuItem
import com.exal.grocerease.view.adapter.MenuProfileAdapter
import com.exal.grocerease.viewmodel.ProfileViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var emailManager: EmailManager

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    private var usernameBinding: String? = "-"
    private var emailBinding: String? = "-"
    private var imageProfileBinding: String? = "-"
    private var totalExpenseBinding: Int? = 0
    private var totalItemsBinding: Int? = 0

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
        viewModel.getAccount()

        observeAccount()

        val menuItems = listOf(
            MenuItem("Pengaturan Akun", R.drawable.ic_account_settings),
            MenuItem("Pengaturan Aplikasi", R.drawable.ic_setting),
            MenuItem("Keluar", R.drawable.ic_logout)
        )

        val adapter = MenuProfileAdapter(requireContext(), menuItems)
        binding.listViewMenu.adapter = adapter

        binding.listViewMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    val intent = Intent(requireContext(), AccountSettingsActivity::class.java)
                    startActivity(intent)
                }
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
    }

    private fun observeAccount() {
        viewModel.accountData.observe(viewLifecycleOwner){ resource ->
            when (resource){
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.setChartData(resource.data?.data?.listsByWeek)

                    totalExpenseBinding = resource.data?.data?.listsByWeek?.sumOf { it?.totalExpenses ?: 0 }
                    totalItemsBinding = resource.data?.data?.listsByWeek?.sumOf { it?.totalItems ?: 0 }

                    usernameBinding = resource.data?.data?.userProfile?.username
                    emailBinding = resource.data?.data?.userProfile?.email
                    imageProfileBinding = resource.data?.data?.userProfile?.image

                    binding.username.text = usernameBinding
                    binding.email.text = emailBinding
                    Glide.with(requireContext())
                        .load(imageProfileBinding)
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.placeholder)
                                .error(R.drawable.ic_close)
                        )
                        .into(binding.profileImage)

                    binding.expensesTv.text = totalExpenseBinding?.let { rupiahFormatter(it) }
                    binding.totalItemsTv.text = buildString {
                        append(totalItemsBinding.toString())
                        append(" ")
                        append(getString(R.string.items))
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun logoutObserver() {
        viewModel.logoutState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    FirebaseMessaging.getInstance().deleteToken()
                    tokenManager.clearToken()
                    emailManager.clearEmail()
                    viewModel.clearDatabase()
                    requireActivity().finish()
                    val intent = Intent(requireContext(), LandingActivity::class.java)
                    startActivity(intent)
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
