package me.henritom.actionsv2.util

import me.henritom.actionsv2.regions.RegionManager

object FunctionUtil {

    fun functionStringToBoolean(function: String): Boolean {
        val parts = function.split("(", limit = 2)
        if (parts.size != 2)
            return functionToBoolean(function, emptyList())

        val functionName = parts[0].trim()
        val argsPart = parts[1].removeSuffix(")")
        val args = if (argsPart.isBlank())
            emptyList()
        else
            argsPart.split(",").map { it.trim() }

        return functionToBoolean(functionName, args)
    }


    fun functionToBoolean(function: String, args: List<String>): Boolean {
        println("function: $function, args: $args")
        return when (function.lowercase()) {
            "isinregion" -> {
                if (args.size != 1)
                    return false

                val region = RegionManager.getRegion(args[0]) ?: return false

                RegionUtil.playerIsInRegion(region)
            }

            else -> false
        }
    }
}