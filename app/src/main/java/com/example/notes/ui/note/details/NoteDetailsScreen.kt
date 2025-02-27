package com.example.notes.ui.note.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.AppBar
import com.example.notes.R
import com.example.notes.ui.AppViewModelProvider
import com.example.notes.utils.toDateStr

@Composable
fun NoteDetailsScreen(
    noteId: Int,
    navigateToEditNote: (Int) -> Unit,
    navigateUp: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: NoteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    viewModel.setNoteId(noteId)
    val uiState = viewModel.noteDetailsUiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppBar(
                title = "",
                canNavigateBack = true,
                navigateUp = navigateUp,
                actions = {
                    IconButton(
                        onClick = { uiState.value.note?.let { navigateToEditNote(it.id) } }
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = uiState.value.note?.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.dp_16))
            )
            Text(
                text = uiState.value.note?.timestamp?.let { toDateStr(it) } ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(dimensionResource(R.dimen.dp_16))
            )
            Text(
                text = uiState.value.note?.content ?: "",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.dp_16))
            )

            if (showDeleteDialog) {
                DeleteConfirmDialog(
                    onConfirm = {
                        showDeleteDialog = false
                        viewModel.deleteNote {
                            navigateBack()
                        }
                    },
                    onDismiss = {
                        showDeleteDialog = false
                    }
                )
            }

        }
    }
}

@Composable
fun DeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.confirm)) },
        text = { Text(stringResource(R.string.delete_confirm_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}