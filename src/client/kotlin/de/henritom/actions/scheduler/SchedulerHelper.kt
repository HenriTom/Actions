package de.henritom.actions.scheduler

class SchedulerHelper {

    fun getNextAvailableID(runningActions: List<ActionScheduler>): Int {
        for (runID in 1..Int.MAX_VALUE)
            if (runningActions.none { it.runID == runID })
                return runID

        return -1
    }

    fun endAllActions(): Int {
        ArrayList(ActionScheduler.runningActions).forEach { it.end() }
        return 0
    }

    fun getSchedulerByRunID(runID: Int): ActionScheduler? {
        return ActionScheduler.runningActions.find { it.runID == runID }
    }
}