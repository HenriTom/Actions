package me.henritom.actionsv2.axn.task

object TaskRegistry {
    private val registeredTasks: MutableMap<String, AxnTask> = mutableMapOf()
    val registeredTaskTypes: MutableSet<TaskType> = mutableSetOf()

    fun initTypes() {
        TaskType("wait", "Waits for a specified amount of time before proceeding",
            listOf(
                TaskSetting("time", "The amount of time to wait before proceeding", "string", "1s", true)
            )
        )

        TaskType("log", "Logs a message with a specified level",
            listOf(
                TaskSetting("message", "The message to log", "string", "", true),
                TaskSetting("level", "The log level to use", "list(debug,error,fatal,info,trace,warn)", "info", false)

            )
        )

        TaskType("if", "Executes tasks based on a specified condition.",
            listOf(
                TaskSetting("condition", "The condition to evaluate.", "string", "", true),
                TaskSetting("length", "The number of tasks to execute if the condition is true.", "number", "1", false)
            )
        )

        TaskType("else", "Executes tasks if the previous 'if' condition is false",
            listOf(
                TaskSetting("length", "The number of tasks to execute if the previous 'if' condition is false", "number", "1", false)
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

        registeredTasks[task.type] = task

        return true
    }
}