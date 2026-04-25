package com.ushyaku.ushyakuminiapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ushyaku.ushyakuminiapp.data.local.TaskEntity
import com.ushyaku.ushyakuminiapp.databinding.ItemTaskBinding

/**
 * Adapter class for the RecyclerView to display a list of tasks.
 * Uses ListAdapter for efficient item updates.
 */
class TaskAdapter(
    private val onEdit: (TaskEntity) -> Unit, // Callback for edit action
    private val onDelete: (TaskEntity) -> Unit // Callback for delete action
) : ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the item layout using ViewBinding
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Bind the data to the view holder
        val task = getItem(position)
        holder.bind(task)
    }

    /**
     * ViewHolder class that holds references to the views for each task item.
     */
    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(taskEntity: TaskEntity) {
            // Set task title and description
            binding.tvTitle.text = taskEntity.title
            binding.tvContent.text = taskEntity.description
            
            // Set click listeners for edit and delete buttons
            binding.btnEdit.setOnClickListener { onEdit(taskEntity) }
            binding.btnDelete.setOnClickListener { onDelete(taskEntity) }
        }
    }

    /**
     * DiffCallback to efficiently calculate differences between two lists.
     */
    class DiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(old: TaskEntity, new: TaskEntity) = old.id == new.id
        override fun areContentsTheSame(old: TaskEntity, new: TaskEntity) = old == new
    }
}