package com.example.tarlauygulamasi.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.util.resource.Resource
import com.example.tarlauygulamasi.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = view.findViewById(R.id.eMail)
        password = view.findViewById(R.id.password)
        loginBtn = view.findViewById(R.id.loginButton)
        registerBtn = view.findViewById(R.id.registerButton)



        email.setText(viewModel.loginEmailInput.value ?: "")
        password.setText(viewModel.loginPasswordInput.value ?: "")


        //direkt giriş için
        viewModel.login("mert@gmail.com", "123456")


        loginBtn.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(emailText, passwordText)
        }

        registerBtn.setOnClickListener {

            viewModel.loginEmailInput.value = email.text.toString()
            viewModel.loginPasswordInput.value = password.text.toString()

            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {

                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Giriş başarılı", Toast.LENGTH_SHORT).show()

                    //Uygulama girişine yönlendir
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