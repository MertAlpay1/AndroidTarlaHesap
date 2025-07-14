package com.example.tarlauygulamasi.ui.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch



@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding?=null
    private val binding get()=_binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)


        _binding=FragmentHomeBinding.inflate(inflater,container,false)
        val view=binding.root

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        var username: String? ="rıfkı"

        viewLifecycleOwner.lifecycleScope.launch {
            val user=viewModel.getUsername()
            username=user?.username
            (requireActivity() as AppCompatActivity).supportActionBar?.title = username
        }


        (requireActivity() as AppCompatActivity).supportActionBar?.title = username

        binding.newfieldButton.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_createNewFieldFragment)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                signoutConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun signoutConfirmationDialog(){

        val builder=AlertDialog.Builder(requireContext())
        builder.setTitle("Çıkış")
        builder.setMessage("Çıkış yapmak istediğinize emin misiniz?")

        builder.setPositiveButton("Evet"){dialog, which ->

            viewModel.signout()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            dialog.dismiss()

        }

        builder.setNegativeButton("Hayır"){dialog, which ->
            dialog.dismiss()

        }

        builder.show()
    }


    //kullanıcı adı en yukarıda

    //En son eklenen tarlaları home da göster boş gözükmesin

}