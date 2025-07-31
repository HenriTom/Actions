package me.henritom.actionsv2.scheduler

import kotlinx.coroutines.*
import me.henritom.actionsv2.axn.AxnAction
import me.henritom.actionsv2.axn.AxnContext
import org.apache.logging.log4j.LogManager

class ActionScheduler(private val action: AxnAction) {

    private val logger = LogManager.getLogger("Actions/Scheduler/${action.id}")
    private val scope = CoroutineScope(Dispatchers.Default)

    private var currentTaskIndex = 0
    private var running = false
    private var alive = false
    private var speed = 1.0

    val isAlive
        get() = alive

    private var job: Job? = null

    fun start(): ActionScheduler {
        if (running || alive)
            return this

        running = true
        alive = true
        currentTaskIndex = 0

        job = scope.launch {
            runLoop()
        }

        return this
    }

    fun pause() {
        running = false
    }

    fun resume() {
        running = true
    }

    fun kill() {
        running = false
        alive = false
        job?.cancel()
        job = null
    }

    fun jumpTo(taskIndex: Int) {
        if (taskIndex !in action.tasks.indices) {
            logger.error("Task index out of bounds: $taskIndex")
            return
        }

        currentTaskIndex = taskIndex
    }

    fun speedUp(factor: Double) {
        if (factor <= 0) {
            logger.error("Speed factor must be greater than 0")
            return
        }

        speed *= factor
    }

    fun slowDown(factor: Double) {
        if (factor <= 0) {
            logger.error("Speed factor must be greater than 0")
            return
        }

        speed /= factor
    }

    private suspend fun runLoop() {
        while (alive && currentTaskIndex < action.tasks.size) {
            while (!running && alive)
                delay(10)

            try {
                val extraData = mapOf("speed" to speed)
                action.tasks[currentTaskIndex].execute(AxnContext(action = action, scheduler = this), extraData)
            } catch (e: Exception) {
                logger.error("Error executing task at index $currentTaskIndex: ${e.message}", e)
            }

            currentTaskIndex++
        }

        action.complete()
    }
}