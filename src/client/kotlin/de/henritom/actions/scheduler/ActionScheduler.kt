package de.henritom.actions.scheduler

import de.henritom.actions.actions.Action

class ActionScheduler(val action: Action) {

    companion object {
        var helper = SchedulerHelper()
        val runningActions = mutableListOf<ActionScheduler>()
    }

    var currentTask = 0
    var cancelled = false
    val runID = helper.getNextAvailableID(runningActions)

    fun start(): ActionScheduler {
        runningActions.add(this)

        currentTask = 0
        cancelled = false

        return this
    }

    fun end(): ActionScheduler {
        runningActions.remove(this)

        currentTask = 0
        cancelled = true

        return this
    }
}