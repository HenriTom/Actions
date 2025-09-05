package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask

class EndTask: AxnTask("end") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        context.scheduler.kill()

        return true
    }
}