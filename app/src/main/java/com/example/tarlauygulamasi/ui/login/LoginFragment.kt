package com.example.tarlauygulamasi.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.databinding.FragmentLoginBinding
import com.example.tarlauygulamasi.util.resource.Resource
import com.example.tarlauygulamasi.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?=null
    private val binding get()=_binding!!

    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentLoginBinding.inflate(inflater, container, false)
        val view=binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.eMail.setText(viewModel.loginEmailInput.value ?: "")
        binding.password.setText(viewModel.loginPasswordInput.value ?: "")




        val auth_Check=viewModel.checkAuth()
        if(auth_Check){
            Log.d("Giriş","Auth çıkmamış")
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        //direkt giriş için
        viewModel.login("mert@gmail.com", "123456")

        binding.loginButton.setOnClickListener {
            val emailText = binding.eMail.text.toString().trim()
            val passwordText = binding.password.text.toString().trim()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(emailText, passwordText)
        }

        binding.registerButton.setOnClickListener {

            viewModel.loginEmailInput.value = binding.eMail.text.toString()
            viewModel.loginPasswordInput.value = binding.password.text.toString()

            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.loginResult.collect { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Giriş başarılı", Toast.LENGTH_SHORT).show()
                    clearInputs()
                    viewModel.loginResult.value = Resource.Loading()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

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

    }
    fun clearInputs() {
        viewModel.loginEmailInput.value = ""
        viewModel.loginPasswordInput.value = ""
    }



}