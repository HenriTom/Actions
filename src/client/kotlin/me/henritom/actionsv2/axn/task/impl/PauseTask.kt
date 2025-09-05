package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask

class PauseTask: AxnTask("pause") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        context.scheduler.pause()

        return true
    }
}