package com.valdo.refind.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.valdo.refind.R
import com.valdo.refind.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Interaction flags
    private var hasUsernameBeenInteractedWith = false
    private var hasEmailBeenInteractedWith = false
    private var hasPasswordBeenInteractedWith = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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

        val textLogin = view.findViewById<TextView>(R.id.textLogin)
        textLogin.setOnClickListener {
            navigateToLogin()
        }

        setupValidation()
        setupClickListeners()
    }

    private fun navigateToLogin() {
        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
        viewPager.currentItem = 0  // Switch to the second tab (RegisterFragment)
        val fragment = LoginFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.register_fragment, fragment)  // Replace with your container ID
            .addToBackStack(null)  // Optional: Add to back stack if you want to go back
            .commit()
    }

    private fun setupClickListeners() {
        binding.buttonRegister.setOnClickListener {
            // Mark all fields as interacted
            hasUsernameBeenInteractedWith = true
            hasEmailBeenInteractedWith = true
            hasPasswordBeenInteractedWith = true

            // Trigger final validation
            if (isValidInput()) {
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
            } else {
                validateUsername(binding.edRegisterUsername.text.toString())
                validateEmail(binding.edRegisterEmail.text.toString())
                validatePassword(binding.edRegisterPassword.text.toString())
            }
        }
    }

    private fun setupValidation() {
        // Username validation
        binding.edRegisterUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (hasUsernameBeenInteractedWith) {
                    validateUsername(s.toString())
                }
            }
        })
        binding.edRegisterUsername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasUsernameBeenInteractedWith = true
                validateUsername(binding.edRegisterUsername.text.toString())
            }
        }

        // Email validation
        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (hasEmailBeenInteractedWith) {
                    validateEmail(s.toString())
                }
            }
        })
        binding.edRegisterEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasEmailBeenInteractedWith = true
                validateEmail(binding.edRegisterEmail.text.toString())
            }
        }

        // Password validation
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (hasPasswordBeenInteractedWith) {
                    validatePassword(s.toString())
                }
            }
        })
        binding.edRegisterPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasPasswordBeenInteractedWith = true
                validatePassword(binding.edRegisterPassword.text.toString())
            }
        }
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

        return binding.usernameContainer.error == null &&
                binding.emailContainer.error == null &&
                binding.passwordContainer.error == null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
