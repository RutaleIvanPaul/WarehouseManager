package io.ramani.ramaniWarehouse.domain.stockreceive.model.selected

import android.graphics.Bitmap

/**
 * @description     Signature Info
 *
 * @author          Adrian
 */
data class SignatureInfo(
    var name: String = "",
    var bitmap: Bitmap? = null
)