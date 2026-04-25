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

/**
 * The main activity of the application that displays a list of notes and allows
 * users to add, edit, or delete them.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout for modern Android devices
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust padding to handle system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize dependencies and ViewModel
        initialSetUp()

        // Setup the RecyclerView to display notes
        setupRecyclerView()

        // Set up click listener for the Floating Action Button to add a new note
        binding.fabAdd.setOnClickListener { showAddDialog() }
    }

    /**
     * Initializes the database, repository, and ViewModel.
     * Also sets up observers and search functionality.
     */
    private fun initialSetUp() {
        val database = NoteDatabase.getDatabase(this)
        val noteDao = database.getNotesDao()
        val repository = NoteRepository(noteDao)

        // ViewModel initialization using a factory
        val factory = NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        // Observe the list of notes from the ViewModel
        viewModel.allNotes.observe(this) { notes->
            adapter.submitList(notes)
        }

        // Add a text watcher to the search field for real-time filtering
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                // Update the search query in the ViewModel
                viewModel.setSearchQuery(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Displays a dialog to edit an existing note.
     */
    private fun showEditDialog(note: Note) {
        val dialogBinding = DialogNoteActionBinding.inflate(layoutInflater)

        // Pre-fill the dialog with existing note data
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
                    // Update the note in the ViewModel
                    val updatedNote =
                        note.copy(noteTitle = updatedTitle, noteDescription = updatedContent)
                    viewModel.update(updatedNote)
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Set dialog background to white
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getColor(this, R.color.white).toDrawable()
        )

        // Setup custom title view components
        titleView.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
            dialog.dismiss()
        }
        titleView.findViewById<TextView>(R.id.tv_title).text = "Update Note"
        dialog.show()
    }

    /**
     * Displays a dialog to add a new note.
     */
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
                    // Insert the new note via the ViewModel
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

    /**
     * Configures the RecyclerView with its adapter and layout manager.
     */
    private fun setupRecyclerView() {
        // Initialize the adapter with callback functions for edit and delete actions
        adapter = NoteAdapter(
            onEdit = { note -> showEditDialog(note) },
            onDelete = { note -> viewModel.delete(note) }
        )

        // Attach the adapter and layout manager
        binding.rvNotes.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }
}