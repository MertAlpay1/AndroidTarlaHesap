package com.example.tarlauygulamasi.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.util.resource.Resource
import com.example.tarlauygulamasi.ui.register.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel: SignupViewModel by viewModels()
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signupBtn: Button
    private lateinit var returnBtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = view.findViewById(R.id.username)
        email = view.findViewById(R.id.eMail)
        password = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.confirmPassword)
        signupBtn = view.findViewById(R.id.signupButton)
        returnBtn = view.findViewById(R.id.returnLoginPage)

        email.setText(viewModel.signupEmailInput.value ?: "")
        password.setText(viewModel.signupPasswordInput.value ?: "")
        username.setText(viewModel.signupUsernameInput.value ?: "")
        confirmPassword.setText(viewModel.signupConfirmPasswordInput.value ?: "")

        signupBtn.setOnClickListener {

            val usernameText = username.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmPasswordText = confirmPassword.text.toString().trim()

            if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(requireContext(), "Geçersiz e-posta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                Toast.makeText(requireContext(), "Şifre 6 karakterden uzun olmalı", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText != confirmPasswordText) {
                Toast.makeText(requireContext(), "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(usernameText, emailText, passwordText)
        }

        returnBtn.setOnClickListener {
            viewModel.signupEmailInput.value = email.text.toString()
            viewModel.signupPasswordInput.value = password.text.toString()
            viewModel.signupUsernameInput.value = username.text.toString()
            viewModel.signupConfirmPasswordInput.value = confirmPassword.text.toString()
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        viewModel.registerResult.observe(viewLifecycleOwner){ result->

            when(result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Kayıt Başarılı", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message ?: "Hata", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {

                }
            }


        }

    }
}
