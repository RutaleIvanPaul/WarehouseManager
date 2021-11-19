package io.ramani.ramaniWarehouse.app.common.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import io.ramani.ramaniWarehouse.app.common.download.DownloadMediaType
import io.ramani.ramaniWarehouse.app.common.download.DownloadingMediaCashedList
import io.ramani.ramaniWarehouse.app.common.download.IFilesDownloadManager
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import java.io.File

class FilesDownloadManager(private val context: Context,
                           private val headersProvider: HeadersProvider) : IFilesDownloadManager {

    private val downloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

    override fun enqueue(url: String, fileName: String, mimeType: String) {
        val file = File(context.getExternalFilesDir(null), fileName)
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
        DownloadingMediaCashedList.add(Pair(downloadId, DownloadMediaType.DOCUMENT))
    }
}