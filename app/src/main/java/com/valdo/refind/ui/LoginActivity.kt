package com.valdo.refind.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.valdo.refind.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupValidation()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            if (isValidInput()) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupValidation() {

        // Email validation
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
            }
        })

        // Password validation
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
            }
        })
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
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        validateEmail(email)
        validatePassword(password)

        return binding.emailContainer.error == null &&
                binding.passwordContainer.error == null
    }
}