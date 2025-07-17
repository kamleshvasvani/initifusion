package com.kamleshpractical.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kamleshpractical.database.Note
import com.kamleshpractical.database.NoteViewModel
import com.kamleshpractical.utils.ErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NoteViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val notes by viewModel.notes.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val focusManager = LocalFocusManager.current


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notes") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        title.isBlank() -> {
                            viewModel.setError("Title cannot be empty")
                        }

                        content.isBlank() -> {
                            viewModel.setError("Content cannot be empty")
                        }

                        isEditing && noteToEdit != null -> {
                            // Update existing note
                            val updatedNote = noteToEdit!!.copy(
                                title = title,
                                content = content,
                                updatedAt = System.currentTimeMillis()
                            )
                            viewModel.updateNote(updatedNote)
                            isEditing = false
                            noteToEdit = null
                            title = ""
                            content = ""
                        }

                        else -> {
                            // Add new note
                            viewModel.addNote(title, content)
                            title = ""
                            content = ""
                            focusManager.clearFocus()
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (isEditing) "Update Note" else "Add Note")
            }


            errorMessage?.let {
                ErrorDialog(message = it, onDismiss = { viewModel.clearError() })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onUpdate = {
                            title = it.title
                            content = it.content
                            isEditing = true
                            noteToEdit = it
                        },
                        onDelete = { viewModel.deleteNote(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onUpdate: (Note) -> Unit, onDelete: (Note) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onUpdate(note) }) {
                    Text("Update")
                }
                TextButton(onClick = { onDelete(note) }) {
                    Text("Delete")
                }
            }
        }
    }
}