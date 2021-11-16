package io.ramani.ramaniWarehouse.domainCore.io

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import io.ramani.ramaniWarehouse.domainCore.io.exceptions.FileCopyErrorException
import io.ramani.ramaniWarehouse.domainCore.log.logDebug
import io.reactivex.Completable
import io.reactivex.Observable
import org.apache.commons.io.IOUtils
import java.io.*


/**
 * Created by Amr on 6/20/17.
 */
class FileHelper(val context: Application) : IFileHelper {

    override fun getFile(uri: Uri, isCaptured: Boolean?): File = Observable.fromCallable {
        var file: File? = null
        var error = ""
        var isError = false

        uri.getFileMetaData(context, isCaptured ?: false) { name, ext, size ->
            var fileOutputStream: OutputStream? = null
            var inputStream: InputStream? = null
            try {
                file = createOutputFile(name, ext)

                fileOutputStream = FileOutputStream(file)

                inputStream = when {
                    uri.isVirtualFile(context) -> getInputStreamForVirtualFile(context, uri, "*/*")
                    else -> context.contentResolver.openInputStream(uri)
                }

                val written = IOUtils.copy(inputStream, fileOutputStream)

                if (written > -1) {
                    if (written < size) {
                        error = ""
                        file = null
                    }
                } else {
                    file = null
                }
            } catch (ex: Exception) {
                error = ex.message ?: ""
                isError = true
            } finally {
                fileOutputStream?.close()
                inputStream?.close()
            }
        }

        if (isError) {
            throw FileCopyErrorException(error)
        } else {
            file ?: throw FileCopyErrorException(error)
        }
    }.blockingFirst()

    override fun getFileType(uri: Uri): String = context.contentResolver.getType(uri)?:""

    override fun getFilePath(uri: Uri): String = FileUtils.getPathFromUri(context, uri) ?: ""

    override fun readFileBytesFromUri(uri: Uri): ByteArray {
        val file = File(uri.path?:"")
        if (file.exists() && file.canRead()) {
            return file.readBytes()
        } else {
            throw FileNotFoundException("File at $uri not found or cannot read")
        }
    }

    override fun getFileSize(file: File): Long = file.length()

    fun getFileSizeObservable(file: File): Observable<Long> = Observable.fromCallable { getFileSize(file) }

    override fun getFileExtension(file: File): String = file.extension

    fun getFileExtensionsObservable(file: File): Observable<String> = Observable.fromCallable { getFileExtension(file) }

    override fun getFileThumbnail(file: File): File? =
        ThumbnailUtils.createVideoThumbnail(file.path, MediaStore.Images.Thumbnails.MINI_KIND)?.let {
            saveFileThumbnail(it, file.name)
        }

    override fun deleteFile(file: File): Completable = Completable.fromAction {
        val deleted = file.delete()
        logDebug("isDeleted $deleted")
    }


    override fun deleteMultiMediaFileByUri(uri: Uri, type: Int): Boolean {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = when (type) {
            1 -> cursor?.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
            else -> cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        }
        val realImagePath = cursor?.getString(idx?:0)
        val realPathFile = File(realImagePath?:"")
        if (realPathFile.exists()) {
            val isDeleted = realPathFile.delete()
            if (isDeleted) {
                refreshMediaScanning(realImagePath?:"")
            }
            return isDeleted
        }
        return false
    }

    private fun saveFileThumbnail(bitmap: Bitmap, name: String): File =
        File.createTempFile("thumbnail_$name", "png", context.cacheDir).apply {
            val os = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        }

    @Throws(IOException::class)
    private fun createOutputFile(outputFileName: String, extension: String): File {
        val name = outputFileName.let {
            if (extension.isNotBlank() && outputFileName.split("\\.").last() != extension) it.plus(".$extension")
            else it
        }
        val saveFileDir = context.filesDir
        val outFile = File(saveFileDir.absolutePath, name)
        if (!outFile.exists()) outFile.createNewFile()

        return outFile
    }

    @Throws(IOException::class)
    private fun getInputStreamForVirtualFile(context: Context, uri: Uri, mimeTypeFilter: String): InputStream {
        val contentResolver = context.contentResolver
        val openableMimeTypes: Array<String>? = contentResolver.getStreamTypes(uri, mimeTypeFilter)

        return if (openableMimeTypes?.isNotEmpty() == true) {
            contentResolver.openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)?.createInputStream()?: FileInputStream("")
        } else {
            throw FileNotFoundException()
        }
    }

    public fun refreshMediaScanning(filePath: String) {
        var paths: MutableList<String> = mutableListOf()
        paths.add(filePath)
        MediaScannerConnection.scanFile(context,
                                        paths.toTypedArray(),
                                        null,
                                        object : MediaScannerConnection.OnScanCompletedListener {
                                            override fun onScanCompleted(path: String?, uri: Uri?) {

                                            }
                                        })
    }

}