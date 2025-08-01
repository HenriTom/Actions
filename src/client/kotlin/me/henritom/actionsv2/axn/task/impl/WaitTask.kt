package me.henritom.actionsv2.axn.task.impl

import kotlinx.coroutines.delay
import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask
import me.henritom.actionsv2.util.TimeUtil

class WaitTask: AxnTask("wait") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        delay((TimeUtil.parseTimeString(data["time"] as String) / (extraData["speed"] as? Double ?: 1.0)).toLong())

        return true
    }
}