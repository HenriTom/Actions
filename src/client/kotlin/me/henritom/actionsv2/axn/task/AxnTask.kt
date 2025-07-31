package me.henritom.actionsv2.axn.task

import me.henritom.actionsv2.axn.AxnContext

abstract class AxnTask (
    val type: String,
    val data: MutableMap<String, Any> = mutableMapOf()
) {
    abstract suspend fun execute(context: AxnContext, extraData: Map<String, Any> = mapOf()): Boolean
}