package io.ramani.ramaniWarehouse.core.domain.validation

/**
 * Created by Amr on 11/20/17.
 */
object ValidationTestHelper {
    object ErrorMessages {
        const val MSG_03_FIELD_REQUIRED = "Please fill in this field"
        const val MSG_25_END_TIME_BEFORE_START_TIME = "Start time should be before end time."
        const val MSG_26_REPLY_DATE_AFTER_EVENT_DATE = "Reply date should not exceed event date."
        const val MSG_27_EVENT_IN_THE_PAST = "You cannot set an event in the past."
        const val MSG_34_HOLIDAY_END_DATE_BEFORE_START_DATE = "Holiday end date cannot be earlier than start date."
        const val MSG_35_HOLIDAY_REPLY_DATE_AFTER_START_DATE = "Reply date should not exceed holiday start date."
        const val MSG_39_FILE_SIZE_TOO_BIG = "File Size Is Too Big, Maximum is"
        const val MSG_44_EXTENSION_NOT_ALLOWED = "Only jpg, jpeg, jpe, jfif, tif, tiff, png, bmp, dib, gif are allowed."
        const val MSG_53_AT_LEAST_ONE_HOLIDAY_DAY_REQUIRED = "At least one day must be added to the holiday."
    }
}