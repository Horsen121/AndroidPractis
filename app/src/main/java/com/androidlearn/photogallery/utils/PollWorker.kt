package com.androidlearn.photogallery.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.androidlearn.photogallery.MyPhotoGallery
import com.androidlearn.photogallery.NOTIFICATION_CHANNEL_ID
import com.androidlearn.photogallery.R
import com.androidlearn.photogallery.api.FlickrFetchr
import com.androidlearn.photogallery.model.GalleryItem

class PollWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    companion object {
        const val ACTION_SHOW_NOTIFICATION = "com.androidlearn.photogallery.SHOW_NOTIFICATION"
        const val PERM_PRIVATE = "com.androidlearn.photogallery.PRIVATE"
        const val REQUEST_CODE = "REQUEST_CODE"
        const val NOTIFICATION = "NOTIFICATION"
    }

    override fun doWork(): Result {
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)
        val items: List<GalleryItem> = if (query.isEmpty()) {
            FlickrFetchr().fetchPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } else {
            FlickrFetchr().searchPhotosRequest(query)
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } ?: emptyList()
        if (items.isEmpty()) {
            return Result.success()
        }
        val resultId = items.first().id
        if (resultId != lastResultId)
            QueryPreferences.setLastResultId(context, resultId)

        val intent = MyPhotoGallery.newIntent(context)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val resources = context.resources
        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.new_pictures_title))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.new_pictures_title))
            .setContentText(resources.getString(R.string.new_pictures_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        showBackgroundNotification(0, notification)

        return Result.success()
    }

    private fun showBackgroundNotification(
        requestCode: Int,
        notification: Notification
    ) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION, notification)
        }
        context.sendOrderedBroadcast(intent, PERM_PRIVATE)
    }
}
