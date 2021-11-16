package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class ClickableSpan : ClickableSpan() {

    var onClick: () -> Unit = {}

    override fun onClick(widget: View) {
        onClick()
    }

    override fun updateDrawState(textPaint: TextPaint) {
        super.updateDrawState(textPaint)
        textPaint.isUnderlineText = false
    }

}
