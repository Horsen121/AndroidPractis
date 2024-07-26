package com.androidlearn.criminalintent.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    onDismiss: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    showState: MutableState<Boolean>,
    dateState: DatePickerState,
    timeState: TimePickerState
) {
    DatePickerDialog(
        onDismissRequest = {
            if (onDismiss != null) {
                onDismiss()
            }
            showState.value = false
        },
        confirmButton = {
            Button(
                onClick = {
                    showState.value = false
                    if (onConfirm != null) {
                        onConfirm()
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    if (onDismiss != null) {
                        onDismiss()
                    }
                    showState.value = false
                }
            ) {
                Text("CANCEL")
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .verticalScroll(rememberScrollState())
        ) {
            DatePicker(state = dateState)
            TimePicker(state = timeState)
        }
    }
}