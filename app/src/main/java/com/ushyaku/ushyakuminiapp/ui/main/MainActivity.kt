package com.ushyaku.ushyakuminiapp.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ushyaku.ushyakuminiapp.R
import com.ushyaku.ushyakuminiapp.data.local.Note
import com.ushyaku.ushyakuminiapp.data.local.NoteDatabase
import com.ushyaku.ushyakuminiapp.data.repo.NoteRepository
import com.ushyaku.ushyakuminiapp.databinding.ActivityMainBinding
import com.ushyaku.ushyakuminiapp.databinding.DialogNoteActionBinding
import com.ushyaku.ushyakuminiapp.ui.viewmodel.NoteViewModel
import com.ushyaku.ushyakuminiapp.ui.viewmodel.NoteViewModelFactory
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialSetUp()

        setupRecyclerView()

        binding.fabAdd.setOnClickListener { showAddDialog() }
    }

    private fun initialSetUp() {
        val database = NoteDatabase.getDatabase(this)

        // 3. Initialize the DAO
        val noteDao = database.getNotesDao()

        // 4. Initialize the Repository with the DAO
        val repository = NoteRepository(noteDao)

        // 5. Initialize the ViewModel using a Factory
        // We use a Factory because NoteViewModel has a constructor parameter (repository)
        val factory = NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        viewModel.allNotes.observe(this) { notes->
            adapter.submitList(notes)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()

                // Pass the query to the ViewModel
                // This triggers the switchMap logic to filter your notes
                viewModel.setSearchQuery(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showEditDialog(note: Note) {

        val dialogBinding = DialogNoteActionBinding.inflate(layoutInflater)

        // Pre-fill the existing data
        dialogBinding.etNoteTitle.setText(note.noteTitle)
        dialogBinding.etDescription.setText(note.noteDescription)

        val titleView = layoutInflater.inflate(R.layout.dialog_title_view, null)

        val dialog = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(dialogBinding.root)
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = dialogBinding.etNoteTitle.text.toString()
                val updatedContent = dialogBinding.etDescription.text.toString()

                if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                    // Create a copy of the note with the same ID but new data
                    val updatedNote =
                        note.copy(noteTitle = updatedTitle, noteDescription = updatedContent)
                    viewModel.update(updatedNote)
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.window?.setBackgroundDrawable(
            ContextCompat.getColor(this, R.color.white).toDrawable()
        )

        titleView.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
            dialog.dismiss()
        }
        titleView.findViewById<TextView>(R.id.tv_title).text = "Update Note"
        dialog.show()
    }

    private fun showAddDialog() {

        val titleView = layoutInflater.inflate(R.layout.dialog_title_view, null)
        val dialogBinding = DialogNoteActionBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogBinding.etNoteTitle.text.toString()
                val content = dialogBinding.etDescription.text.toString()

                if (title.isNotEmpty() && content.isNotEmpty()) {
                    viewModel.insert(Note(noteTitle = title, noteDescription = content))
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.window?.setBackgroundDrawable(
            ContextCompat.getColor(this, R.color.white).toDrawable()
        )

        titleView.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
            dialog.dismiss()
        }
        titleView.findViewById<TextView>(R.id.tv_title).text = "Add New Note"

        dialog.show()
    }

    private fun setupRecyclerView() {
        // Initialize the adapter with click logic
        adapter = NoteAdapter(
            onEdit = { note -> showEditDialog(note) },
            onDelete = { note -> viewModel.delete(note) }
        )
        binding.rvNotes.adapter = adapter

        // Attach the adapter and layout manager
        binding.rvNotes.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            // Optional: Add a smooth animation for item changes
            setHasFixedSize(true)
        }
    }
}