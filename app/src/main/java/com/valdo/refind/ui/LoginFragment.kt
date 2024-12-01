package com.valdo.refind.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.valdo.refind.R
import com.valdo.refind.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var hasEmailBeenInteractedWith = false
    private var hasPasswordBeenInteractedWith = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the Forgot Password TextView and set its click listener
        val forgotPassword = view.findViewById<TextView>(R.id.textForgotPassword)
        val textRegister = view.findViewById<TextView>(R.id.textRegister)
        forgotPassword.setOnClickListener {
            navigateToForgotPassword()
        }

        textRegister.setOnClickListener {
            navigateToRegister()
        }

        setupValidation()
        setupClickListeners()
    }

    private fun navigateToRegister() {
        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
        viewPager.currentItem = 1  // Switch to the second tab (RegisterFragment)
        val fragment = RegisterFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.login_fragment, fragment)  // Replace with your container ID
            .addToBackStack(null)  // Optional: Add to back stack if you want to go back
            .commit()
    }

    private fun navigateToForgotPassword() {
        // Manually navigate to the ForgotPasswordFragment using FragmentTransaction
        val fragment = ForgotPasswordFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.authActivity, fragment)  // Replace with your container ID
            .addToBackStack(null)  // Optional: Add to back stack if you want to go back
            .commit()
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            hasEmailBeenInteractedWith = true
            hasPasswordBeenInteractedWith = true

            if (isValidInput()) {
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RegisterFragment())  // Navigate to RegisterFragment
                    .addToBackStack(null)
                    .commit()
            } else {
                validateEmail(binding.edLoginEmail.text.toString())
                validatePassword(binding.edLoginPassword.text.toString())
            }
        }
    }

    private fun setupValidation() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (hasEmailBeenInteractedWith) {
                    validateEmail(s.toString())
                }
            }
        })
        binding.edLoginEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasEmailBeenInteractedWith = true
                validateEmail(binding.edLoginEmail.text.toString())
            }
        }

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (hasPasswordBeenInteractedWith) {
                    validatePassword(s.toString())
                }
            }
        })
        binding.edLoginPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasPasswordBeenInteractedWith = true
                validatePassword(binding.edLoginPassword.text.toString())
            }
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
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        validateEmail(email)
        validatePassword(password)

        return binding.emailContainer.error == null &&
                binding.passwordContainer.error == null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

