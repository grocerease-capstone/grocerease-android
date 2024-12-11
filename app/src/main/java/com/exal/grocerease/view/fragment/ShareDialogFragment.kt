package com.exal.grocerease.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.exal.grocerease.R
import com.exal.grocerease.databinding.AddManualDialogFragmentBinding
import com.exal.grocerease.databinding.FragmentShareDialogBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.Detail
import com.exal.grocerease.model.network.response.ProductsItem
import com.exal.grocerease.view.activity.CreateListActivity
import com.exal.grocerease.view.activity.DetailExpenseActivity
import com.exal.grocerease.viewmodel.DetailExpenseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ShareDialogFragment(private val id: Int) : DialogFragment() {

    private var _binding: FragmentShareDialogBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            val email = binding.textFieldName.editText?.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                Log.d("ShareDialogFragment", "Email: $email, ID: $id")
                (activity as? DetailExpenseActivity)?.viewModel?.shareList(id, email)?.collect{ resource ->
                    (activity as? DetailExpenseActivity)?.handleShareResource(resource)
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}