package io.ramani.android.app.pdfviewer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.barteksc.pdfviewer.PDFView
import io.ramani.android.app.pdfviewer.sources.UrlSource

class PdfViewerWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var pdfView: PDFView? = null

    init {
        pdfView = PDFView(context, attrs)
        pdfView?.layoutParams = LayoutParams(context, attrs).apply {
            width = LayoutParams.MATCH_PARENT
            height = LayoutParams.MATCH_PARENT
        }
        addView(pdfView)
    }

    fun fromUrl(url: String,
                headers: Map<String, String> = mapOf(),
                onLoad: () -> Unit = {},
                onError: (Throwable) -> Unit = {},
                onPageError: (Int, Throwable) -> Unit = { _, _ -> },
                onPageChange: (Int, Int) -> Unit = { _, _ -> }) {
        pdfView?.fromSource(UrlSource(url, headers))
                ?.onLoad {
                    onLoad()
                }
                ?.onError {
                    onError(it)
                }
                ?.onPageError { page, t ->
                    onPageError(page, t)
                }
                ?.onPageChange { page, pageCount ->
                    onPageChange(page, pageCount)
                }
                ?.load()
    }
}