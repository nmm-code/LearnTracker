package com.nmm_code.learntracker.logic

import java.util.Locale

object TimeUtils {
    fun formatSeconds(seconds: Long, locale: Locale = Locale.getDefault()): String {
        val hours = (seconds % 86400) / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        val parts = mutableListOf<String>()

        val hourLabel = "h"
        val minuteLabel = "min"
        val secondLabel = "s"

        if (hours > 0) {
            parts.add("$hours $hourLabel")
        }
        if (minutes > 0) {
            parts.add("$minutes $minuteLabel")
        }

        if (minutes == 0L) {
            parts.add("$remainingSeconds $secondLabel")
        }

        return parts.joinToString(" ")
    }
}