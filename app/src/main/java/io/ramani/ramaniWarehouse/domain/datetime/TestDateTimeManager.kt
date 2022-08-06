package io.ramani.ramaniWarehouse.domain.datetime

import io.ramani.ramaniWarehouse.domainCore.datetime.IDateTimeManager

/**
 * Created by Amr on 12/10/17.
 */
class TestDateTimeManager : IDateTimeManager {


    companion object {
        const val DISPLAY_DATE_FORMAT = "dd MMM"
        const val DISPLAY_TIME_FORMAT = "HH:mm"
        const val WEEK_START_SUNDAY = 1
        const val UTC = "utc"
    }
    override fun is24Hours(): Boolean = true


}