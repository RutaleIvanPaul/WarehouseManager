package io.ramani.ramaniWarehouse.domainCore.io

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import io.ramani.ramaniWarehouse.core.domain.extension.takeIfNotBlank


/**
 * Created by Amr on 11/20/17.
 */
@SuppressLint("Range")
fun Uri.getFileMetaData(
    context: Context,
    isCaptured: Boolean = false,
    onResult: (String, String, Long) -> Unit
) {
    var fileName = ""
    var fileSize = 0L

    val contentResolver = context.contentResolver
    val intentFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION

    if (!isCaptured) {
        contentResolver.takePersistableUriPermission(this, intentFlags)
    }

    val cursor = contentResolver.query(this, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))

            if (fileName.isEmpty()) {
                val index = path?.lastIndexOf('/') ?: 0
                if (index != -1) {
                    fileName = path?.substring(index + 1) ?: ""
                }
            }

            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            if (!it.isNull(sizeIndex)) {
                fileSize = it.getString(sizeIndex)?.toLongOrNull() ?: 0
            }
        }
    }

    var fileExtension:String = fileName.extension.takeIfNotBlank() ?: extension(context)

    onResult(fileName, fileExtension, fileSize)
}

val String.extension: String
    get() = substringAfterLast('.', "")

fun Uri.extension(context: Context) =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(this)) ?: ""

fun Uri.isVirtualFile(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= 24) {
        if (!DocumentsContract.isDocumentUri(context, this)) {
            return false
        }

        val cursor: Cursor? = context.contentResolver.query(
            this,
            arrayOf(DocumentsContract.Document.COLUMN_FLAGS),
            null,
            null,
            null
        )

        val flags: Int = cursor?.use {
            if (cursor.moveToFirst()) {
                cursor.getInt(0)
            } else {
                0
            }
        } ?: 0

        return flags and DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT != 0
    } else {
        return false
    }
}



