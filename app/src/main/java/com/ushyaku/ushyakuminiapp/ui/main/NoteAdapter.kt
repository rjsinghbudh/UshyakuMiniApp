package com.ushyaku.ushyakuminiapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ushyaku.ushyakuminiapp.data.local.Note
import com.ushyaku.ushyakuminiapp.databinding.ItemNoteBinding

/**
 * Adapter class for the RecyclerView to display a list of notes.
 * Uses ListAdapter for efficient item updates.
 */
class NoteAdapter(
    private val onEdit: (Note) -> Unit, // Callback for edit action
    private val onDelete: (Note) -> Unit // Callback for delete action
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Inflate the item layout using ViewBinding
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Bind the data to the view holder
        val note = getItem(position)
        holder.bind(note)
    }

    /**
     * ViewHolder class that holds references to the views for each note item.
     */
    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            // Set note title and description
            binding.tvTitle.text = note.noteTitle
            binding.tvContent.text = note.noteDescription
            
            // Set click listeners for edit and delete buttons
            binding.btnEdit.setOnClickListener { onEdit(note) }
            binding.btnDelete.setOnClickListener { onDelete(note) }
        }
    }

    /**
     * DiffCallback to efficiently calculate differences between two lists.
     */
    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(old: Note, new: Note) = old.id == new.id
        override fun areContentsTheSame(old: Note, new: Note) = old == new
    }
}