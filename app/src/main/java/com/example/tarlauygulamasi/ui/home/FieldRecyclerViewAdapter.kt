package com.example.tarlauygulamasi.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.model.AdapterClass
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.databinding.FieldItemViewBinding
import com.example.tarlauygulamasi.util.toFormattedArea
import java.util.Locale

class FieldRecyclerViewAdapter(
    private var fieldList: List<Field> = emptyList(),
    private val onItemClick: (Field) -> Unit,
    private val onItemLongClick: (Field) -> Unit,
) : RecyclerView.Adapter<FieldRecyclerViewAdapter.FieldViewHolder>(), Filterable {

    private val diffUtil=object : DiffUtil.ItemCallback<Field>(){
        override fun areItemsTheSame(
            oldItem: Field,
            newItem: Field
        ): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Field,
            newItem: Field
        ): Boolean {
            return oldItem==newItem
        }
    }
    private var asyncListDiffer = AsyncListDiffer(this, diffUtil)

    inner class FieldViewHolder(val binding: FieldItemViewBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder   {
        val binding= FieldItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FieldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.binding.FieldName.text = item.name
        holder.binding.FieldArea.text = item.area.toFormattedArea()

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
    fun updateFields(newList: List<Field>) {
        fieldList = newList
        asyncListDiffer.submitList(newList)
    }

    fun isFilteredFieldEmpty(): Boolean{

        return asyncListDiffer.currentList.isEmpty()
    }



    override fun getFilter(): Filter? {

        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults? {

                val query=constraint?.toString()?.lowercase(Locale.ROOT)?.trim() ?:""

                val result = if (query.isEmpty()) {
                    fieldList
                } else {
                    fieldList.filter {
                        it.name.lowercase().contains(query)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = result
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredList  =results?.values as List<Field>
                asyncListDiffer.submitList(filteredList)
            }
        }
    }
}

//diffutil.