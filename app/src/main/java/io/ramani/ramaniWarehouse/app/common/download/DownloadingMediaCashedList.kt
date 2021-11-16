package io.ramani.ramaniWarehouse.app.common.download

import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull


object DownloadingMediaCashedList {

    private var inProgressDownloadList = mutableListOf<Pair<Long, Int>>()

    fun checkIsMediaDownloadingInProgress(downloadId: Long): Boolean =
            inProgressDownloadList.find {
                it.first == downloadId
            }?.isNotNull() ?: false

    fun getMediaDownloadingFileType(downloadId: Long): Int =
            inProgressDownloadList.find {
                it.first == downloadId
            }?.second ?: DownloadMediaType.PHOTO

    fun removeDownloadId(downloadId: Long) {
        val downloadPairRecord = inProgressDownloadList.find {
            it.first == downloadId
        }
        downloadPairRecord?.let {
            inProgressDownloadList.remove(it)
        }
    }

    fun add(record: Pair<Long, Int>) {
        inProgressDownloadList.add(record)
    }

}