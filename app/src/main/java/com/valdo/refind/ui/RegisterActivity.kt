package com.valdo.refind.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.valdo.refind.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupValidation()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonRegister.setOnClickListener {
            if (isValidInput()) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupValidation() {
        // Username validation
        binding.edRegisterUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateUsername(s.toString())
            }
        })

        // Email validation
        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
            }
        })

        // Password validation
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
            }
        })
    }

    private fun validateUsername(username: String) {
        if (username.isEmpty()) {
            binding.usernameContainer.error = "Username cannot be empty"
        } else {
            binding.usernameContainer.error = null
        }
    }

    private fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isEmpty()) {
            binding.emailContainer.error = "Email cannot be empty"
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.emailContainer.error = "Invalid email format"
        } else {
            binding.emailContainer.error = null
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            binding.passwordContainer.error = "Password cannot be empty"
        } else if (password.length < 8) {
            binding.passwordContainer.error = "Password must be at least 8 characters"
        } else {
            binding.passwordContainer.error = null
        }
    }

    private fun isValidInput(): Boolean {
        val username = binding.edRegisterUsername.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        validateUsername(username)
        validateEmail(email)
        validatePassword(password)

        return binding.emailContainer.error == null &&
                binding.passwordContainer.error == null
    }
}
