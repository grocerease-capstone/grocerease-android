package com.exal.grocerease.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.exal.grocerease.databinding.ActivityResetPasswordBinding
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.UpdateListResponse
import com.exal.grocerease.viewmodel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()
    private var username: String? = null
    private var passwordOld: String? = null
    private var passwordNew: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        username = intent.getStringExtra("USERNAME")
        Log.d("USERNAME ACTIVITY", "Username: $username")

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            handlePasswordChange()
        }
    }

    private fun handlePasswordChange() {
        passwordOld = binding.oldPassInputTxt.editText?.text.toString().trim()
        passwordNew = binding.newPassInputTxt.editText?.text.toString().trim()
        val repeatPassword = binding.repeatPassInputTxt.editText?.text.toString().trim()

        when {
            passwordOld!!.isEmpty() -> {
                showToast("Please enter your old password.")
            }
            passwordNew!!.isEmpty() -> {
                showToast("Please enter a new password.")
            }
            repeatPassword.isEmpty() -> {
                showToast("Please repeat the new password.")
            }
            passwordNew != repeatPassword -> {
                showToast("New Password and Repeat Password do not match.")
            }
            passwordNew!!.length < 8 -> {
                showToast("New Password must be at least 8 characters.")
            }
            else -> {
                handleSaveButtonClick()
            }
        }
    }

    private fun createRequestBody(value: String?): okhttp3.RequestBody =
        value.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull())

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

    private fun handleSaveButtonClick() {
        lifecycleScope.launch {
            val usernameReqBody = createRequestBody(username)
            Log.d("USERNAME ACTIVITY", "Username: $username")
            val profileImagePart = createImagePart("profile_image", null)
            val password = createRequestBody(passwordOld)
            val passwordNew = createRequestBody(passwordNew)

            viewModel.updateData(
                profileImagePart,
                usernameReqBody,
                password,
                passwordNew
            ).collect { resource ->
                handleResource(resource)
            }
        }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
