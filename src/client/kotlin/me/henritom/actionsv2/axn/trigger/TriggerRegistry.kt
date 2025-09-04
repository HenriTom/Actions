package me.henritom.actionsv2.axn.trigger

object TriggerRegistry {
    private val registeredTriggers: MutableMap<AxnTrigger, String> = mutableMapOf()
    val registeredTriggerTypes: MutableSet<TriggerType> = mutableSetOf()

    fun initTypes() {
        TriggerType("biome_update", "Triggers, when the biome is updated",
            listOf(
                TriggerSetting("biome", "The biome to watch for changes", "string"), TriggerSetting("type", "The type of the biome update", "list(enter,leave)", "enter", true)
            )
        )

        TriggerType("call", "Triggers, by manually calling the action",
            listOf(
                TriggerSetting("privilege_level", "The privilege level the action executes in", "int", 0, false)
            )
        )

        TriggerType("chat", "Triggers, when a specified chat message is sent or received",
            listOf(
                TriggerSetting("message", "The message to watch for", "string"),
                TriggerSetting("condition", "Defines how the message content should be matched", "list(any,contains,contains,equals,equals_ic,starts,ends)", "any"),
                TriggerSetting("invert", "If true, the trigger will be triggered when the message does not match the condition", "boolean", defaultValue = false),
                TriggerSetting("type", "The type of the chat trigger", "list(sent,received)", "received", false)
            )
        )

        TriggerType("connection", "Triggers, when the player connects or disconnects",
            listOf(
                TriggerSetting("type", "The type of the connection event", "list(connect,disconnect)", "connect")
            )
        )

        TriggerType("inventory", "Triggers, when the inventory is updated",
            listOf(
                TriggerSetting("type", "The type of the inventory update", "list(any,contains_any,contains_exact,contains_less,contains_more)", "any"),
                TriggerSetting("item", "The item to check for", "string", required = false),
                TriggerSetting("amount", "The amount of the item to check for", "int", defaultValue = 1, required = false),
            )
        )

        TriggerType("keybind", "Triggers, when a keybind is pressed",
            listOf(
                TriggerSetting("key", "The key to watch for", "string", required = false),
                TriggerSetting("action", "The action to watch for", "number(0-2)", required = false),
                TriggerSetting("modifiers", "The modifiers to watch for", "string", required = false)
            )
        )

        TriggerType("region_update", "Triggers, when the region is updated",
            listOf(
                TriggerSetting("region", "The region to watch for changes", "string"),
                TriggerSetting("type", "The type of the region update", "list(enter,leave)", "enter")
            )
        )

        TriggerType("respawn", "Triggers, when the player respawns")

        TriggerType("tick", "Triggers, every tick")

        TriggerType("variable_update", "Triggers, when a given variable is updated",
            listOf(
                TriggerSetting("variable", "The variable to watch for changes", "string")))

        TriggerType("world_change", "Triggers, when the player changes the world",
            listOf(
                TriggerSetting("type", "The type of the world change event", "list(enter,leave)", "enter")
            )
        )
    }

    fun registerTriggerType(triggerType: TriggerType): Boolean {
        if (registeredTriggerTypes.contains(triggerType))
            return false

        registeredTriggerTypes.add(triggerType)

        return true
    }

    fun registerTrigger(trigger: AxnTrigger): Boolean {
        if (registeredTriggers.containsKey(trigger))
            return false

        registeredTriggers[trigger] = trigger.type

        return true
    }

    fun triggerAll(triggerType: String, callArgs: String) {
        for ((trigger, type) in registeredTriggers)
            if (triggerType == type)
                trigger.trigger(callArgs)
    }

    fun getTriggersByType(triggerType: String): List<AxnTrigger> {
        val triggers = mutableListOf<AxnTrigger>()

        for ((trigger, type) in registeredTriggers)
            if (triggerType == type)
                triggers.add(trigger)

        return triggers
    }
}