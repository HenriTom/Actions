package de.henritom.actions.variables

class LocalVariableStorage {

    private val variables = mutableMapOf<String, Any>()

    init {
        update()
    }

    fun addVariable(name: String, value: Any) {
        variables[name] = value
    }

    fun removeVariable(name: String) {
        variables.remove(name)
    }

    fun getVariable(name: String): Any? {
        return variables[name]
    }

    fun update(): LocalVariableStorage {
        variables.clear()

        variables["rat"] = "tus"
        variables["rat2"] = "tus2"

        return this
    }
}