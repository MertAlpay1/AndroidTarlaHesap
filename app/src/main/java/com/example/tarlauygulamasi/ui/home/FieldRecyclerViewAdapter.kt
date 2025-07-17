package com.example.tarlauygulamasi.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.model.AdapterClass
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

    private var filteredFieldList = listOf<Field>()

    inner class FieldViewHolder(val binding: FieldItemViewBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder   {
        val binding= FieldItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FieldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val item = filteredFieldList[position]
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
        return filteredFieldList.size
    }
    fun updateFields(newList: List<Field>) {
        fieldList = newList
        filteredFieldList = newList
        notifyDataSetChanged()
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
                filteredFieldList=results?.values as List<Field>
                notifyDataSetChanged()
            }
        }
    }


}