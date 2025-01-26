package com.example.notes.ui.note.edit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.R
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Int? = savedStateHandle["noteId"]

    private var _editNoteUiState = mutableStateOf(EditNoteUiState())
    val editNoteUiState: State<EditNoteUiState> = _editNoteUiState

    init {
        Log.d("id", noteId.toString())
    }

    fun onTitleTextChanged(text: String) {
        _editNoteUiState.value = _editNoteUiState.value.copy(
            titleText = text,
            titleErrorMsgRes = null
        )
    }

    fun onContentTextChanged(text: String) {
        _editNoteUiState.value = _editNoteUiState.value.copy(
            contentText = text
        )
    }

    private fun isInputValid(): Boolean {
        if (_editNoteUiState.value.titleText.isEmpty()) {
            _editNoteUiState.value = _editNoteUiState.value.copy(
                titleErrorMsgRes = R.string.title_empty_warning
            )
            return false
        }
        return true
    }

    fun saveNote(callback: () -> Unit) {
        if (isInputValid()) {
            val note = _editNoteUiState.value.toNote()
            viewModelScope.launch {
                notesRepository.insertNote(note)
                callback()
            }
        }
    }

}