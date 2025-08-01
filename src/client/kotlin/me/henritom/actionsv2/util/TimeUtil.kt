package me.henritom.actionsv2.util

import kotlin.math.abs

object TimeUtil {
    fun parseTimeString(timeStr: String): Long {
        val trimmed = timeStr.trim()

        if (Regex("""^(\d{1,2}:){1,3}\d{1,2}$""").matches(trimmed)) {
            val parts = trimmed.split(":").map { it.toLongOrNull() ?: 0 }
            val (days, hours, minutes, seconds) = when (parts.size) {
                2 -> listOf(0L, 0L, parts[0], parts[1])
                3 -> listOf(0L, parts[0], parts[1], parts[2])
                4 -> listOf(parts[0], parts[1], parts[2], parts[3])
                else -> listOf(0L, 0L, 0L, 0L)
            }

            return days * 86_400_000 +
                    hours * 3_600_000 +
                    minutes * 60_000 +
                    seconds * 1_000
        }

        var totalMillis = 0.0

        val regex = Regex("""(\d+(?:\.\d+)?)\s*(ms|d|h|m|s)?""", RegexOption.IGNORE_CASE)

        for (match in regex.findAll(trimmed)) {
            val (valueStr, unit) = match.destructured
            val value = valueStr.toDoubleOrNull() ?: continue

            val millis = when (unit.lowercase()) {
                "d"  -> value * 86_400_000
                "h"  -> value * 3_600_000
                "m"  -> value * 60_000
                "s"  -> value * 1_000
                else -> value
            }

            totalMillis += millis
        }

        return abs(totalMillis).toLong()
    }
}
