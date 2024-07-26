package com.androidlearn.criminalintent.features.crime.view.addEditCrime

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.androidlearn.criminalintent.R
import com.androidlearn.criminalintent.utils.DateDialog
import com.androidlearn.criminalintent.utils.getScaledBitmap
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrimeScreen(
    context: Context,
    navController: NavController,
    viewModel: CrimeViewModel = viewModel(
        modelClass = CrimeViewModel::class.java,
        factory = CrimeViewModel.Factory
    )
) {
    BackHandler(viewModel.title.isNotEmpty()) {
        viewModel.saveCrime()
        navController.navigateUp()
    }

    val openDialog = remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState(initialSelectedDateMillis = viewModel.date.time)
    val timeState = rememberTimePickerState(is24Hour = true)
    if (openDialog.value) {
        DateDialog(
            onConfirm = {
                dateState.selectedDateMillis?.let {
                    viewModel.date = Date(it + LocalTime.of(timeState.hour, timeState.minute).toSecondOfDay()*1000)
                }
            },
            showState = openDialog,
            dateState = dateState,
            timeState = timeState
        )
    }

    val contactIntent = Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    }
    val getSuspect = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val contactUri: Uri? = result.data?.data
            val projection: Array<String> = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            contactUri?.let {
                context.contentResolver.query(it, projection, null, null, null).use { cursor ->
                    if (cursor!!.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        val name = cursor.getString(nameIndex)
                        viewModel.suspect = name
                    }
                }
            }
        }
    }
    val getPhoto = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { result ->
        if (result) viewModel.photo = viewModel.getUri(context)
    }


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    if (viewModel.photo != null) {
                        Image(
                            bitmap = getScaledBitmap(viewModel.getPhotoFile().path, 400, 400),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Image(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(onClick = { getPhoto.launch(viewModel.getUri(context)) }) {
                        Image(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    }
                }
                Column {
                    Text(text = stringResource(id = R.string.crime_title_label))
                    TextField(
                        value = viewModel.title,
                        onValueChange = { viewModel.title = it },
                        placeholder = { Text(text = stringResource(R.string.crime_title_hint)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { openDialog.value = true }) {
                Text(text = viewModel.date.toString())
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.isSolved,
                    onCheckedChange = { viewModel.isSolved = it },
                    modifier = Modifier.padding(8.dp)
                )
                Text(text = stringResource(id = R.string.crime_solved_label))
            }
            Button(onClick = { getSuspect.launch(contactIntent) }) {
                Text(text = viewModel.suspect.ifEmpty { stringResource(id = R.string.button_crime_suspect_text) })
            }
            Button(onClick = {
                Intent(Intent.ACTION_SEND).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, viewModel.getCrimeReport(context))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        context.getString(R.string.crime_report_subject)
                    )
                }.also { intent ->
                    startActivity(context , intent, null)
                }
            }) {
                Text(text = stringResource(id = R.string.button_crime_report_text))
            }
        }
        
        Button(
            onClick = {
                viewModel.saveCrime()
                navController.navigateUp()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.button_save))
        }
    }
}

@Preview
@Composable
private fun Prev() {
    CrimeScreen(context = LocalContext.current, navController = NavController(LocalContext.current))
}