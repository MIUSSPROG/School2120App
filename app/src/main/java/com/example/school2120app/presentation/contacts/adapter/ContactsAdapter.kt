package com.example.school2120app.presentation.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.school2120app.databinding.ContactRvItemBinding
import com.example.school2120app.domain.model.contacts.ContactInfo

class ContactsAdapter: ListAdapter<ContactInfo, ContactsAdapter.ContactsViewHolder>(DiffCallback()) {

    class ContactsViewHolder(private val binding: ContactRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ContactInfo){
            binding.apply {
                tvNameRvContactItem.text = item.name
                tvPositionRvContactItem.text = item.position
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<ContactInfo>(){
        override fun areItemsTheSame(oldItem: ContactInfo, newItem: ContactInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ContactInfo, newItem: ContactInfo): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ContactRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem)
    }
}