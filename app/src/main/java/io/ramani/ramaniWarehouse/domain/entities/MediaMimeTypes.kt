package io.ramani.ramaniWarehouse.domain.entities

/**
 * Created by habib on 2/25/18.
 */
object MediaMimeTypes {
    enum class Documents(val mime: String) {
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        PDF("application/pdf"),
        DOC("application/msword"),
        PPT("application/vnd.ms-powerpoint"),
        PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        PPSX("application/vnd.openxmlformats-officedocument.presentationml.slideshow"),
        XLS("application/vnd.ms-excel"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        CSV("text/csv"),
        TXT("text/plain"),
        EPUP("application/epub+zip"),
        IMAGE("image/jpeg")
    }
}