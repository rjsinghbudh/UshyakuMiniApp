TaskMaster - Mini Android Task Manager

A robust, lightweight Android application designed to manage daily tasks efficiently. This project demonstrates the implementation of modern Android development practices, including MVVM architecture, local persistence with Room, and reactive UI updates using LiveData.
🚀 Features

    Task List: View all tasks in a clean, organized RecyclerView.

    CRUD Operations: Full support to Create, Read, Update, and Delete tasks.

    Local Persistence: Data is stored locally using Room Database, ensuring your tasks are available offline.

    Real-time Search: (Bonus) Quickly filter through your tasks using the integrated search functionality.

    Input Validation: Robust error handling to prevent empty entries and ensure data integrity.

🛠 Architecture & Tech Stack

The application follows the MVVM (Model-View-ViewModel) architectural pattern to ensure a separation of concerns and a testable codebase.

    Language: Kotlin

    Database: Room Persistence Library

    Lifecycle: ViewModel and LiveData for reactive data streams.

    UI Components: * RecyclerView for efficient list rendering.

        FloatingActionButton for task creation.

        Material Design 3 components for a modern look and feel.

    Concurrency: Kotlin Coroutines for non-blocking database operations.

📂 Project Structure
Plaintext

com.example.taskmaster/
├── data/
│   ├── local/
│   │   ├── TaskDatabase.kt      # Room Database configuration
│   │   ├── TaskDao.kt           # Data Access Object for CRUD
│   │   └── TaskEntity.kt        # Database Schema
│   └── repository/
│       └── TaskRepository.kt    # Single source of truth for data
├── ui/
│   ├── adapter/
│   │   └── TaskAdapter.kt       # RecyclerView Adapter
│   ├── viewmodel/
│   │   └── TaskViewModel.kt     # Business logic & LiveData management
│   └── view/
│       ├── MainActivity.kt      # Main task list screen
│       └── AddEditTaskActivity.kt # Task entry/modification screen

🚥 Getting Started
Prerequisites

    Android Studio Ladybug or newer.

    JDK 17.

    Android SDK Level 24+ (Minimum).

Installation

    Clone the repository:
    Bash

    git clone https://github.com/your-username/task-manager-android.git

    Open the project in Android Studio.

    Sync the project with Gradle files.

    Run the application on an emulator or a physical device.

📝 Usage

    Add a Task: Click the + button on the main screen, fill in the title and description, and save.

    Edit a Task: Tap on any existing task to modify its details.

    Delete a Task: Long-press a task or use the delete icon within the edit screen to remove it.

    Search: Use the search bar at the top to filter tasks by title in real-time.
