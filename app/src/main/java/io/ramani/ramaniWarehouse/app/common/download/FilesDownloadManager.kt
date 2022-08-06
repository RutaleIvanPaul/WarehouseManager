package io.ramani.ramaniWarehouse.app.common.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import java.io.File

class FilesDownloadManager(
    private val context: Context,
    private val headersProvider: HeadersProvider
) : IFilesDownloadManager {

    private val downloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

    override fun enqueue(
        url: String,
        fileName: String,
        mimeType: String,
        onComplete: (Uri) -> Unit
    ) {
        val directory = File(
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath}/${BuildConfig.APP_NAME}/")

        if (!directory.exists())
            directory.mkdir()

        val file = File(directory, fileName)
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setMimeType(mimeType)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverRoaming(true)
            .setAllowedOverMetered(true)
            .apply {
                headersProvider.getHeaders().entries.forEach { (key, value) ->
                    addRequestHeader(key, value)
                }

                allowScanningByMediaScanner()
            }
        val downloadId = downloadManager.enqueue(request)
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {
                if (downloadId == -1L) return
                try {
                    val mostRecentDownload: Uri =
                        downloadManager.getUriForDownloadedFile(downloadId)
                    onComplete(mostRecentDownload)
                } catch (e: Exception) {
                }
            }
        }

        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        DownloadingMediaCashedList.add(Pair(downloadId, DownloadMediaType.DOCUMENT))
    }
}