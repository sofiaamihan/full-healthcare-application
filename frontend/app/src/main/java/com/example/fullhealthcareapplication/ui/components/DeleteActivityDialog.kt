package com.example.fullhealthcareapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteActivityDialog(
    onDismiss: () -> Unit,
    onDeleteActivity: (Int) -> Unit,
    id: Int
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("WARNING: Delete Activity") },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteActivity(id)
                }
            ) {
                Text("Delete Activity")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },
        icon = { Icon(Icons.Default.Info, contentDescription = "Delete Warning") }
    )
}