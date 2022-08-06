package io.ramani.ramaniWarehouse.app.common.presentation.language

fun String.replacePlaceHolderWithText(placeHolder: String, textValue: String) =
        replacePlaceHolderWithText(listOf(placeHolder), listOf(textValue))

fun String.replacePlaceHolderWithText(placeHolders: List<String>,
                                      textValues: List<String>): String {
    var replacedText = this
    for (i in placeHolders.indices) {
        replacedText = replacedText.replace(placeHolders[i], textValues[i])
    }
    return replacedText
}