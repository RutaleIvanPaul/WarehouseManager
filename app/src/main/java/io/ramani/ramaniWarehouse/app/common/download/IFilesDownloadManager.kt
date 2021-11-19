package io.ramani.ramaniWarehouse.app.common.download

interface IFilesDownloadManager {
    fun enqueue(url: String, fileName: String, mimeType: String)
}