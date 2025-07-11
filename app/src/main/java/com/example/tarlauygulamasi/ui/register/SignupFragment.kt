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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.databinding.FragmentSignupBinding
import com.example.tarlauygulamasi.util.resource.Resource
import com.example.tarlauygulamasi.ui.register.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding?=null
    private val binding get()=_binding!!
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSignupBinding.inflate(inflater, container, false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.username.setText(viewModel.signupUsernameInput.value ?: "")
        binding.eMail.setText(viewModel.signupEmailInput.value ?: "")
        binding.password.setText(viewModel.signupPasswordInput.value ?: "")
        binding.confirmPassword.setText(viewModel.signupConfirmPasswordInput.value ?: "")

        binding.signupButton.setOnClickListener {

            val usernameText = binding.username.text.toString().trim()
            val emailText =  binding.eMail.text.toString().trim()
            val passwordText =  binding.password.text.toString().trim()
            val confirmPasswordText = binding.confirmPassword.text.toString().trim()

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

        binding.returnLoginPage.setOnClickListener {
            viewModel.signupEmailInput.value = binding.eMail.text.toString()
            viewModel.signupPasswordInput.value = binding.password.text.toString()
            viewModel.signupUsernameInput.value = binding.username.text.toString()
            viewModel.signupConfirmPasswordInput.value = binding.confirmPassword.text.toString()
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){viewModel.registerResult.collect {  result ->
                    when (result) {
                        is Resource.Success -> {
                            Toast.makeText(requireContext(), "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                            clearInputs()
                            viewModel.registerResult.value = Resource.Loading()
                            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)


                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), result.message ?: "Hata", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {

                        }
                    }
                }}




            }

    }

        fun clearInputs() {
           viewModel.signupEmailInput.value = ""
           viewModel.signupPasswordInput.value = ""
           viewModel.signupUsernameInput.value = ""
           viewModel.signupConfirmPasswordInput.value = ""
         }
}
