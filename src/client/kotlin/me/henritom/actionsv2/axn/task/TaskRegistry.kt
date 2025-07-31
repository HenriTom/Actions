package me.henritom.actionsv2.axn.task

object TaskRegistry {
    private val registeredTasks: MutableMap<String, AxnTask> = mutableMapOf()
    private val registeredTaskTypes: MutableSet<TaskType> = mutableSetOf()

    fun initTypes() {
        TaskType("wait", "Waits for a specified amount of time before proceeding.",
            listOf(
                TaskSetting("time", "The amount of time to wait before proceeding.", "string", "1s", true)
            )
        )

        TaskType("log", "Logs a message with a specified level.",
            listOf(
                TaskSetting("level", "The log level to use.", "list(debug,error,fatal,info,trace,warn)", "info", true),
                TaskSetting("message", "The message to log.", "string", "", true)
            )
        )
    }

    fun registerTaskType(taskType: TaskType): Boolean {
        if (registeredTaskTypes.contains(taskType))
            return false

        registeredTaskTypes.add(taskType)

        return true
    }

    fun registerTask(task: AxnTask): Boolean {
        if (registeredTasks.containsValue(task))
            return false

        registeredTasks.put(task.type, task)

        return true
    }
}