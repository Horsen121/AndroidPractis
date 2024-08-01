package com.androidlearn.photogallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.androidlearn.photogallery.ui.theme.PhotoGalleryTheme

class PhotoView : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uri = this.intent.getStringExtra("uri") ?: ""
            PhotoGalleryTheme {
                Scaffold() {
                    PhotoPage(applicationContext, uri)
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent { // , photoPageUri: Uri
            return Intent(context, PhotoView::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                data = photoPageUri
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PhotoPage(
    context: Context,
    uri: String,
    modifier: Modifier = Modifier
) {
    val webView = remember {
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    AndroidView(
        factory = { webView },
        update = {
            it.loadUrl(uri)
        },
        modifier = modifier
            .fillMaxSize()
    )
}