package io.ramani.ramaniWarehouse.domainCore.io

import android.net.Uri
import io.reactivex.Completable
import java.io.File

/**
 * Created by Amr on 11/20/17.
 */
interface IFileHelper {
    fun getFile(uri: Uri, isCaptured: Boolean? = null): File

    fun readFileBytesFromUri(uri: Uri): ByteArray

    fun getFileSize(file: File): Long

    fun getFileExtension(file: File): String

    fun getFileThumbnail(file: File): File?

    fun getFileType(uri: Uri): String

    fun getFilePath(uri: Uri): String

    fun deleteFile(file: File): Completable

    fun deleteMultiMediaFileByUri(uri: Uri, type: Int): Boolean

}