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
import android.widget.Filter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import android.widget.Toast

import androidx.fragment.app.activityViewModels

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch



@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding?=null
    private val binding get()=_binding!!
    private val viewModel: HomeViewModel by activityViewModels()

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

        binding.emptyTextShow.visibility = View.GONE
        binding.emptySearchText.visibility = View.GONE

        val fieldList:Flow<List<Field>> =viewModel.getUserField()

        val adapter = FieldRecyclerViewAdapter(onItemClick = { field ->

            findNavController().navigate(R.id.action_homeFragment_to_fieldDetailFragment, Bundle().apply {
                putLong("fieldId",field.id)
            })
            
        }, onItemLongClick = { field ->
            deleteFieldConfirmationDialog(field.id)
        })

        binding.fieldRecyclerView.adapter=adapter
        binding.fieldRecyclerView.layoutManager= LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUserField().collect { fieldList ->
                if(fieldList.isEmpty()){
                        binding.emptyTextShow.visibility = View.VISIBLE
                    return@collect
                }
                else{
                    binding.emptyTextShow.visibility = View.GONE
                }
                adapter.updateFields(fieldList)
            }
        }

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter?.filter(newText)

                    val isFilteredListEmpty=adapter.isFilteredFieldEmpty()
                    if(isFilteredListEmpty) binding.emptySearchText.visibility = View.VISIBLE
                    else binding.emptySearchText.visibility = View.GONE

                return true
            }

        })


        binding.newfieldButton.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_createNewFieldFragment)

        }
        binding.newfieldButtonByWalking.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_createNewFieldByWalkingFragment)

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
            dialog.dismiss()

            Toast.makeText(requireContext(),"Çıkış başarılı.",
                Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        builder.setNegativeButton("Hayır"){dialog, which ->
            dialog.dismiss()

        }

        builder.show()
    }

    private fun deleteFieldConfirmationDialog(fieldId:Long){

        val builder= AlertDialog.Builder(requireContext())

        builder.setTitle("Tarla Silme")
        builder.setMessage("Silmek istediğinize emin misiniz?")

        builder.setPositiveButton("Evet"){dialog, which ->

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.deleteField(fieldId)
            }
            Toast.makeText(requireContext(),"Tarlanız başarıyla silindi.",
                Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Hayır"){dialog, which ->

            dialog.dismiss()
        }
        builder.show()
    }

}