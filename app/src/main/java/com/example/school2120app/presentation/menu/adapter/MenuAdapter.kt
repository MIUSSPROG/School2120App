package com.example.school2120app.presentation.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.school2120app.core.util.ActionListener
import com.example.school2120app.databinding.MenuRvItemBinding
import com.example.school2120app.domain.model.menu.remote.MenuItem

class MenuAdapter(
    private val actionListener: ActionListener<MenuItem>
): ListAdapter<MenuItem, MenuAdapter.MenuViewHolder>(DiffCallback()) {

    class MenuViewHolder(private val binding: MenuRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(menu: MenuItem){
            binding.apply {
                tvMenuPdfName.text = menu.name
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<MenuItem>(){
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.itemView.setOnClickListener {
            actionListener.onItemClicked(curItem)
        }
        holder.bind(curItem)
    }
}