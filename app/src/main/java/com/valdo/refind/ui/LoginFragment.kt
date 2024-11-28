package com.valdo.refind.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        // Handle insets for keyboard visibility
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures())

            // Adjust the bottom padding of the root view so the keyboard doesn't overlap
            binding.root.setPadding(0, 0, 0, imeInsets.bottom + navInsets.bottom)
            insets
        }

        setupValidation()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            hasEmailBeenInteractedWith = true
            hasPasswordBeenInteractedWith = true

            if (isValidInput()) {
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            } else {
                // Trigger validations in case they were skipped due to lack of interaction
                validateEmail(binding.edLoginEmail.text.toString())
                validatePassword(binding.edLoginPassword.text.toString())
            }
        }
    }

    private fun setupValidation() {
        // Email validation
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

        // Password validation
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
