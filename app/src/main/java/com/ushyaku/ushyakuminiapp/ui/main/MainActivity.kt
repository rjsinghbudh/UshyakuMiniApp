package com.ushyaku.ushyakuminiapp.ui.main

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
import com.ushyaku.ushyakuminiapp.data.local.TaskEntity
import com.ushyaku.ushyakuminiapp.data.local.TaskDatabase
import com.ushyaku.ushyakuminiapp.data.repo.TaskRepository
import com.ushyaku.ushyakuminiapp.databinding.ActivityMainBinding
import com.ushyaku.ushyakuminiapp.databinding.DialogTaskActionBinding
import com.ushyaku.ushyakuminiapp.ui.viewmodel.TaskViewModel
import com.ushyaku.ushyakuminiapp.ui.viewmodel.TaskViewModelFactory
import androidx.core.graphics.drawable.toDrawable

/**
 * The main activity of the application that displays a list of tasks and allows
 * users to add, edit, or delete them.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

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

        // Setup the RecyclerView to display tasks
        setupRecyclerView()

        // Set up click listener for the Floating Action Button to add a new task
        binding.fabAddTask.setOnClickListener { showAddDialog() }
    }

    /**
     * Initializes the database, repository, and ViewModel.
     * Also sets up observers and search functionality.
     */
    private fun initialSetUp() {
        val database = TaskDatabase.getDatabase(this)
        val taskDao = database.taskDao()
        val repository = TaskRepository(taskDao)

        // ViewModel initialization using a factory
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        // Observe the list of tasks from the ViewModel
        viewModel.tasks.observe(this) { tasks->
            adapter.submitList(tasks)
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
     * Displays a dialog to edit an existing task.
     */
    private fun showEditDialog(taskEntity: TaskEntity) {
        val dialogBinding = DialogTaskActionBinding.inflate(layoutInflater)

        // Pre-fill the dialog with existing task data
        dialogBinding.etTaskTitle.setText(taskEntity.title)
        dialogBinding.etDescription.setText(taskEntity.description)

        val titleView = layoutInflater.inflate(R.layout.dialog_title_view, null)

        val dialog = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(dialogBinding.root)
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = dialogBinding.etTaskTitle.text.toString()
                val updatedContent = dialogBinding.etDescription.text.toString()

                if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                    // Update the task in the ViewModel
                    val updatedTask =
                        taskEntity.copy(title = updatedTitle, description = updatedContent)
                    viewModel.update(updatedTask)
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
        titleView.findViewById<TextView>(R.id.tv_title).text = "Update Task"
        dialog.show()
    }

    /**
     * Displays a dialog to add a new task.
     */
    private fun showAddDialog() {
        val titleView = layoutInflater.inflate(R.layout.dialog_title_view, null)
        val dialogBinding = DialogTaskActionBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogBinding.etTaskTitle.text.toString()
                val content = dialogBinding.etDescription.text.toString()

                if (title.isNotEmpty() && content.isNotEmpty()) {
                    // Insert the new task via the ViewModel
                    viewModel.insert(TaskEntity(title = title, description = content))
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
        titleView.findViewById<TextView>(R.id.tv_title).text = "Add New Task"

        dialog.show()
    }

    /**
     * Configures the RecyclerView with its adapter and layout manager.
     */
    private fun setupRecyclerView() {
        // Initialize the adapter with callback functions for edit and delete actions
        adapter = TaskAdapter(
            onEdit = { task -> showEditDialog(task) },
            onDelete = { task -> viewModel.delete(task) }
        )

        // Attach the adapter and layout manager
        binding.rvTasks.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }
}