package com.androidlearn.photogallery

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.androidlearn.photogallery.ui.theme.PhotoGalleryTheme
import com.androidlearn.photogallery.utils.PollWorker

var viewModel: MainViewModel? = null

class MyPhotoGallery : ComponentActivity() {
    private val onShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            resultCode = RESULT_CANCELED
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (viewModel == null) viewModel = MainViewModel(application)
            PhotoGalleryTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = R.string.app_name)) }, // Text(stringResource(id = R.string.app_name))
                            actions = {
                                IconToggleButton(
                                    checked = viewModel!!.isPolling, onCheckedChange = { viewModel!!.toggleTouch(it) }
                                ) {
                                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Notify toggle")
                                }
                                SearchBar(
                                    placeholder = { Text(text = stringResource(id = R.string.search)) },
                                    query = viewModel!!.text,
                                    onQueryChange = { viewModel!!.text = it },
                                    onSearch = {
                                        viewModel!!.searchPhotos(it)
                                    },
                                    active = false,
                                    onActiveChange = {  },
                                    trailingIcon = {
                                        Row(
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            if (viewModel!!.text.isNotEmpty()) {
                                                Icon(
                                                    modifier = Modifier.clickable { viewModel!!.text = "" },
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Close icon"
                                                )
                                            }
                                            Icon(
                                                modifier = Modifier.clickable {
                                                    if (viewModel!!.text.isNotEmpty()) { viewModel!!.searchPhotos(viewModel!!.text)}
                                                },
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search icon"
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                ) { }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MyPhotoGallery::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollWorker.ACTION_SHOW_NOTIFICATION)
            this.registerReceiver(
                onShowNotification,
                filter,
                PollWorker.PERM_PRIVATE,
                null
            )
    }
    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(onShowNotification)
    }
}

@Composable
fun MainScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(vertical = 96.dp, horizontal = 16.dp)
    ) {
        items(viewModel!!.photos) { galleryItem ->
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .clickable {
                        // Open in Browser
//                        val intent = Intent(Intent.ACTION_VIEW, galleryItem.photoPageUri).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        }
                        val intent = PhotoView.newIntent(viewModel!!.getApplication<Application>().applicationContext).apply {
                            putExtra("uri", galleryItem.photoPageUri.toString())
                        }
                        startActivity(viewModel!!.getApplication<Application>().applicationContext, intent, null)
                    }
            ) {
                AsyncImage(
                model = galleryItem.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}