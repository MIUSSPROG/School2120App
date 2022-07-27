package com.example.school2120app.presentation.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.school2120app.databinding.ScheduleRvItemBinding
import com.example.school2120app.domain.model.schedule.local.GradeLesson

class ScheduleAdapter: ListAdapter<GradeLesson, ScheduleAdapter.ScheduleViewHolder>(DiffCallback()) {

    class ScheduleViewHolder(private val binding: ScheduleRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(gradeLesson: GradeLesson, position: Int){
            binding.apply {
                tvScheduleItemRoom.text = gradeLesson.room
                tvScheduleItemLesson.text = "${position + 1}. ${gradeLesson.name}"
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<GradeLesson>() {
        override fun areItemsTheSame(oldItem: GradeLesson, newItem: GradeLesson): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GradeLesson, newItem: GradeLesson): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ScheduleRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem, position)
    }
}