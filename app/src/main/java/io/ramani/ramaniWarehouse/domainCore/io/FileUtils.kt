package io.ramani.ramaniWarehouse.domainCore.io

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.text.format.Formatter
import android.util.Log

import java.io.File

import android.content.ContentValues.TAG

/**
 * Created by Aftab Ali on 8/1/17.
 */

object FileUtils {


    /**
     * Check if the SD card is mounted (whether there is an SD card)
     *
     * @return
     */
    val isMountedSDCard: Boolean
        get() {
            if (Environment.MEDIA_MOUNTED == Environment
                            .getExternalStorageState()) {
                return true
            } else {
                Log.w(TAG, "SDCARD is not MOUNTED !")
                return false
            }
        }


    /**
     * Create directory
     *
     * @param context
     * @param dirName folder name
     * @return
     */
    fun createFileDir(context: Context, dirName: String): File {
        val filePath: String
        // If the SD card already exists, store it; otherwise there is a data directory
        if (isMountedSDCard) {
            // SD卡路径
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + dirName
        } else {
            filePath = context.cacheDir.path + File.separator + dirName
        }
        val destDir = File(filePath)
        if (!destDir.exists()) {
            val isCreate = destDir.mkdirs()
            Log.i("FileUtils", "$filePath has created. $isCreate")
        }
        return destDir
    }

    /**
     * Create a folder (support overwriting the existing folder with the same name)
     *
     * @param filePath
     * @param recreate
     * @return
     */
    fun createFolder(filePath: String, recreate: Boolean): File? {
        val folderName = getFolderName(filePath)
        if (folderName == null || folderName.isEmpty() || folderName.trim { it <= ' ' }.isEmpty()) {
            return null
        }
        val folder: File = File(folderName)
        if (folder.exists()) {
            if (recreate) {
                deleteFile(folderName)
                folder.mkdirs()
            }
        } else {
            folder.mkdirs()

        }
        return folder
    }

    /**
     * Get the folder name
     *
     * @param filePath
     * @return
     */
    fun getFolderName(filePath: String?): String? {
        if (filePath == null || filePath.isEmpty() || filePath.trim { it <= ' ' }.isEmpty()) {
            return filePath
        }
        val filePos = filePath.lastIndexOf(File.separator)
        return if (filePos == -1) "" else filePath.substring(0, filePos)
    }

    fun deleteFile(filename: String): Boolean {
        return File(filename).delete()
    }


    /**
     * Retrieves a File path represented by given Uri
     *
     * @param context
     * @param uri
     * @return
     */
    fun getFileFromUri(context: Context, uri: Uri): File? {
        val path = getPathFromUri(context, uri)
        return if (TextUtils.isEmpty(path))
            null
        else
            File(path!!)
    }

    /**
     * Retrieves a String path represented by given Uri
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    fun getPathFromUri(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (hasAPI(19) && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                return if (id.startsWith("raw:")) {
                    id.split(':').last()
                } else {
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id))

                    getDataColumn(context, contentUri, null, null)
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection,
                        selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection,
                    selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    fun isGoogleDriveUri(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority
    }


    fun hasAPI(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT >= apiLevel
    }


    /**
     * Format the file size
     *
     * @param context
     * @param size
     * @return
     */
    fun formatFileSize(context: Context, size: Long): String {
        return Formatter.formatFileSize(context, size)
    }

}
