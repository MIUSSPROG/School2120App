package com.example.school2120app.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.school2120app.core.util.ActionListener
import com.example.school2120app.core.util.HelperMethods
import com.example.school2120app.databinding.DocRvItemBinding
import com.example.school2120app.domain.model.profile.UserDoc

class DocumentsAdapter(
    private val actionListener: ActionListener<UserDoc>
): ListAdapter<UserDoc, DocumentsAdapter.DocumentsViewHolder>(DiffCallback()){

    inner class DocumentsViewHolder(private val binding: DocRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: UserDoc){
            binding.apply {
                tvDocNameRvItem.text = item.title
                tvEndDateRvItem.text = item.endDate
                if (HelperMethods.isDateToCome(item.endDate)){
                    tvExpiredRvItem.visibility = View.INVISIBLE
                }else{
                    tvExpiredRvItem.visibility = View.VISIBLE
                }
                imgvDocDetailRvItem.setOnClickListener {
                    actionListener.onItemClicked(item)
                }
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<UserDoc>(){
        override fun areItemsTheSame(oldItem: UserDoc, newItem: UserDoc): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserDoc, newItem: UserDoc): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val binding = DocRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem)
    }
}