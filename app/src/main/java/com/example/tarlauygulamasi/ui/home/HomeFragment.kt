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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var newFieldBtn : TextView

    private lateinit var nllBtn : TextView

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar=view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        //Mevcut kullanıcı alınacak
        val username = "rifki"
        (requireActivity() as AppCompatActivity).supportActionBar?.title = username

        newFieldBtn=view.findViewById<TextView>(R.id.newfieldButton)



        newFieldBtn.setOnClickListener {

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