package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask

class PauseTask: AxnTask("pause") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        if (!context.scheduler.isAlive)
            return false

        if (context.scheduler.running)
            context.scheduler.pause()
        else
            context.scheduler.resume()

        return true
    }
}