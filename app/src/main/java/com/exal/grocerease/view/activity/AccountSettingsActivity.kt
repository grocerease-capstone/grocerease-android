package com.exal.grocerease.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ActivityAccountSettingsBinding
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.request.ProductItem
import com.exal.grocerease.model.network.response.UpdateListResponse
import com.exal.grocerease.viewmodel.AccountSettingsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AccountSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSettingsBinding
    private val viewModel: AccountSettingsViewModel by viewModels()
    private var profileImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getAccount()
        observeAccount()

        binding.editBtn.setOnClickListener {
            openGallery()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.resetBtn.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.saveBtn.setOnClickListener {
            handleSaveButtonClick()
        }
    }

    private fun observeAccount() {
        viewModel.accountData.observe(this) {
            binding.usernameInputTxt.editText?.setText(it.data?.data?.userProfile?.username)
            Glide.with(this)
                .load(it.data?.data?.userProfile?.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.ic_close)
                )
                .into(binding.profileImage)
        }
    }

    private fun createRequestBody(value: String?): okhttp3.RequestBody =
        value.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull())

    private fun handleSaveButtonClick() {
        lifecycleScope.launch {
            val username = createRequestBody(binding.usernameInputTxt.editText?.text.toString())
            val profileImagePart = createImagePart("profile_image", profileImage)
            val password = createRequestBody(null)
            val passwordNew = createRequestBody(null)

            viewModel.updateData(
                profileImagePart,
                username,
                password,
                passwordNew
            ).collect { resource ->
                handleResource(resource)
            }
        }
    }

    private fun createImagePart(partName: String, uriPath: String?): MultipartBody.Part? {
        Log.d("CreateListActivity", "Image Path: $uriPath")
        uriPath?.let { path ->
            val uri = Uri.parse(path)
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "temp_image.jpeg")
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(partName, file.name, requestBody)
        }
        return null
    }

    private fun handleResource(resource: Resource<UpdateListResponse>) {
        when (resource) {
            is Resource.Loading -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            is Resource.Success -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("TARGET_FRAGMENT", "ProfileFragment")
                startActivity(intent)
                finish()
            }
            is Resource.Error -> {
                Log.d("USERNAME ACTIVITY", "Error: ${resource.message}")
                Toast.makeText(this, "Error ${resource.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            processSelectedImage(uri)
        } else {
            Snackbar.make(binding.root, "Tidak ada gambar yang dipilih", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun processSelectedImage(uri: Uri) {
        binding.profileImage.setImageURI(uri)
        profileImage = uri.toString()
    }
}