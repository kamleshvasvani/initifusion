package com.kamleshpractical.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val dao: NoteDao
) : ViewModel() {

    val notes = dao.getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun addNote(title: String, content: String) {
        if (title.isBlank() || content.isBlank()) {
            _errorMessage.value = "Title and content must not be blank"
            return
        }

        val note = Note(
            title = title,
            content = content,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        if (note.title.isBlank() || note.content.isBlank()) {
            _errorMessage.value = "Title and content must not be blank"
            return
        }

        val updated = note.copy(updatedAt = System.currentTimeMillis())
        viewModelScope.launch {
            dao.updateNote(updated)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun setError(message: String) {
        _errorMessage.value = message
    }

}
