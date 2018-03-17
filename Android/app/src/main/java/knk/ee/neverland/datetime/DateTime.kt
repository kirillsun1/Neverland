package knk.ee.neverland.datetime

import java.text.SimpleDateFormat
import java.util.*

class DateTime {
    var date: Date? = null
    var time: Time? = null

    override fun toString(): String {
        val c = Calendar.getInstance()
        c.set(date!!.year, date!!.month, date!!.day, time!!.hour, time!!.minute, time!!.second)
        return SimpleDateFormat.getDateTimeInstance().format(c.time)
    }
}