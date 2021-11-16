package io.ramani.ramaniWarehouse.domain.datetime

import io.ramani.ramaniWarehouse.domainCore.datetime.IDateTimeManager


/**
 * Created by Amr on 12/5/17.
 */
class DateTimeManager() : IDateTimeManager {


    companion object {
        private const val DEFAULT_DISPLAY_DATE_FORMAT = "dd MMM"
        private const val ALTERNATIVE_DISPLAY_DATE_FORMAT = "dd MMM yy"
        private const val SERVER_DEFAULT_DISPLAY_DATE_FORMAT = "DD MMM"
        private const val SERVER_ALTERNATIVE_DISPLAY_DATE_FORMAT = "DD MMM YY"
        private const val DEFAULT_DISPLAY_TIME_FORMAT = "hh:mm a"
        private const val TWENTY_FOUR_TIME_FORMAT = "HH:mm"

    }


    override fun is24Hours(): Boolean {
        return true
    }
}