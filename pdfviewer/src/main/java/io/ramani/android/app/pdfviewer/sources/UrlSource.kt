package io.ramani.android.app.pdfviewer.sources

import android.content.Context
import com.github.barteksc.pdfviewer.source.DocumentSource
import com.github.barteksc.pdfviewer.util.Util
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import java.net.HttpURLConnection
import java.net.URL

class UrlSource(val url: String, val headers: Map<String, String>) : DocumentSource {
    override fun createDocument(
            context: Context?,
            core: PdfiumCore?,
            password: String?
    ): PdfDocument {
        try {
            val inputStream = getInputStream(url)
            return core?.newDocument(Util.toByteArray(inputStream))
                    ?: throw IllegalArgumentException("Core is null")
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message)
        }
    }

    private fun getInputStream(url: String) =
            URL(url).run {
                val connection = openConnection() as HttpURLConnection

                headers.entries.forEach { (key, value) ->
                    connection.setRequestProperty(key, value)
                }

                connection.inputStream
            }
}