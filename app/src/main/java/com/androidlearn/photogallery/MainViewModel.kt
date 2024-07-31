package com.androidlearn.photogallery

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.androidlearn.photogallery.api.FlickrFetchr
import com.androidlearn.photogallery.model.GalleryItem
import com.androidlearn.photogallery.utils.PollWorker
import com.androidlearn.photogallery.utils.QueryPreferences
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val POLL_WORK = "POLL_WORK"

class MainViewModel(
    private val application: Application
): AndroidViewModel(application) {
    private val flickrFetchr = FlickrFetchr()

    var text by  mutableStateOf("")
    var isPolling by mutableStateOf(QueryPreferences.isPolling(application.applicationContext))

    var photos by mutableStateOf<List<GalleryItem>>(emptyList())

    init {
        getPhotos()
    }

    fun searchPhotos(query: String) {
        QueryPreferences.setStoredQuery(application, query)
        viewModelScope.launch {
            flickrFetchr.searchPhotos(query).observeForever {
                photos = it ?: emptyList()
            }
        }
    }

    fun toggleTouch(polling: Boolean) {
        isPolling = polling
        if (isPolling) {
            WorkManager.getInstance(application.applicationContext).cancelUniqueWork(POLL_WORK)
            QueryPreferences.setPolling(application.applicationContext, false)
        } else {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            val periodicRequest = PeriodicWorkRequest
                .Builder(PollWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(application.applicationContext).enqueueUniquePeriodicWork(POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest)
            QueryPreferences.setPolling(application.applicationContext, true)
        }
    }

    private fun getPhotos() {
        viewModelScope.launch {
            flickrFetchr.fetchPhotos().observeForever {
                photos = it ?: emptyList()
            }
        }
    }
}
