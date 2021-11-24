package io.ramani.ramaniWarehouse.app.common.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import java.io.File


class DownloadManager(private val context: Context, var dateFormatter: DateFormatter,
                      private val headersProvider: HeadersProvider) : IMediaDownloadManager {

    var downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

    override fun enqueue(downloadUrl: String, mediaType: Int, fileName: String, needsAuth: Boolean,
                         onDownloadComplete: (Uri) -> Unit) {
        var mDownloadedFileID: Long = -1L

        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?,
                                   intent: Intent?) {
                if (mDownloadedFileID == -1L) return
                try {
                    val mostRecentDownload: Uri = downloadManager.getUriForDownloadedFile(mDownloadedFileID)
                    onDownloadComplete(mostRecentDownload)
                } catch (e: Exception) {
                }

                mDownloadedFileID = -1
            }
        }

        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        val warehouseDirectoryPrefix = "WarehouseAps"

        val extension = when (mediaType) {
            DownloadMediaType.REPORT -> "pdf"
            DownloadMediaType.DOCUMENT_CSV -> "csv"
            DownloadMediaType.DOCUMENT_XLSX -> "xlsx"
            DownloadMediaType.DOCUMENT_XLS -> "xls"
            else -> downloadUrl.split(".").last()
        }

        val downloadFileName = when (mediaType) {
            DownloadMediaType.REPORT -> fileName
            else -> if (fileName.isNotEmpty()) {
                "$fileName.$extension"
            } else {
                "pr-" + "" + "." + extension
            }
        }

        val directory = File(Environment.getExternalStoragePublicDirectory(when (mediaType) {
                                                                               DownloadMediaType.PHOTO -> Environment.DIRECTORY_PICTURES
                                                                               DownloadMediaType.VIDEO -> Environment.DIRECTORY_MOVIES
                                                                               else -> Environment.DIRECTORY_DOCUMENTS
                                                                           }).absolutePath + "/$warehouseDirectoryPrefix/")

        if (!directory.exists())
            directory.mkdir()

        val request = DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(downloadFileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(when (mediaType) {
                                                       DownloadMediaType.PHOTO -> Environment.DIRECTORY_PICTURES
                                                       DownloadMediaType.VIDEO -> Environment.DIRECTORY_MOVIES
                                                       else -> Environment.DIRECTORY_DOCUMENTS
                                                   },
                                                   File.separator + warehouseDirectoryPrefix + File.separator + downloadFileName)
                .setAllowedOverRoaming(true)
                .setAllowedOverMetered(true)

        when (mediaType) {
            DownloadMediaType.REPORT,
            DownloadMediaType.DOCUMENT_CSV,
            DownloadMediaType.DOCUMENT_XLSX,
            DownloadMediaType.DOCUMENT_XLS ->
                request.apply {
                    if (needsAuth) {
                        headersProvider.getHeaders().entries.forEach { (key, value) ->
                            addRequestHeader(key, value)
                        }
                    }
                    allowScanningByMediaScanner()
                }
        }

        mDownloadedFileID = downloadManager.enqueue(request)

        DownloadingMediaCashedList.add(Pair(mDownloadedFileID, mediaType))
    }

}