package ua.czrblz.vrg_soft_test_task.utils

import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

fun Long.convertTimestamp(): String {
    val date = Date(this * 1000)
    return PrettyTime().format(date)
}